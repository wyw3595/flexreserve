package com.flexreserve.appointment.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flexreserve.appointment.entity.Appointment;
import com.flexreserve.appointment.entity.BookingConfig;
import com.flexreserve.appointment.entity.BookingOrder;
import com.flexreserve.appointment.entity.dto.OrderCreateDTO;
import com.flexreserve.appointment.entity.dto.OrderPageQueryDTO;
import com.flexreserve.appointment.entity.dto.OrderResultDTO;
import com.flexreserve.appointment.entity.dto.ResourceDTO;
import com.flexreserve.appointment.entity.vo.OrderPageQueryVO;
import com.flexreserve.appointment.mapper.AppointmentMapper;
import com.flexreserve.appointment.mapper.OrderMapper;
import com.flexreserve.appointment.service.OrderService;
import com.flexreserve.common.PageResult;
import com.flexreserve.common.Result;
import com.flexreserve.utils.UserHolder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final StringRedisTemplate stringRedisTemplate;
    private final OrderMapper orderMapper;
    private final AppointmentMapper appointmentMapper;
    private DefaultRedisScript<Long> bookingLuaScript;
    @PostConstruct
    public void initBookingScript(){
        bookingLuaScript = new DefaultRedisScript<>();
        bookingLuaScript.setLocation(new ClassPathResource("createdOrder.lua"));
        bookingLuaScript.setResultType(Long.class);
    }
    @Override
    public Result<OrderResultDTO> createOrder(OrderCreateDTO orderCreateDTO) {
        // 1.获取当前用户ID
        Long userId = UserHolder.getUser().getId();
        // 2.查看资源是否存在且启用
        ResourceDTO resource = appointmentMapper.getResourceById(orderCreateDTO.getResourceId());
        if (resource == null || resource.getStatus() != 1) {
            return Result.error("资源不存在或已停用");
        }
        // 3.根据 resourceId 查询预约配置，校验存在。
        BookingConfig configByResource = appointmentMapper.getConfigByResourceId(orderCreateDTO.getResourceId());
        if (configByResource == null) {
            return Result.error("资源预约配置不存在");
        }
        // 4.校验预约时间是否在可预约范围内
        LocalDate today = LocalDate.now();
        // 4.1.当前时间加上可预约天数
        LocalDate date = today.plusDays(configByResource.getAdvanceDays());
        String bookingDate = orderCreateDTO.getBookingDate();
        LocalDate parse = LocalDate.parse(bookingDate);
        if (parse.isAfter(date)){
            return Result.error("超出可预约天数");
        }
        if (parse.isBefore(today)) {
            return Result.error("日期不能早于今天");
        }
        // 4.3 如果是预约今天，判断时段是否已过期
        if (today.isEqual(parse)) {
            LocalTime now = LocalTime.now();
            LocalTime startTime = LocalTime.parse(orderCreateDTO.getStartTime());
            if (startTime.isBefore(now)) {
                return Result.error("该时段已开始，无法预约");
            }
        }
        // 5.查看当前用户是否有预约过此资源

        //6.用 Redis + Lua 脚本原子检查并扣减该时段库存。扣减失败直接返回"已满"。
        // 6.1 组装数据
        String startTime = orderCreateDTO.getStartTime();
        String bookingDate1 = orderCreateDTO.getBookingDate();
        Long resourceId = orderCreateDTO.getResourceId();
        Integer maxPerSlot = configByResource.getMaxBookingPerSlot();
        String hashKey = "slot:count:" + resourceId + ":" + bookingDate1;
        String setKey = "booking:user:" + resourceId + ":" + bookingDate1 + ":" + startTime;
        List<String> keys = List.of(hashKey, setKey);

        // 6.2. 组装 ARGV（Object 数组）
        Object[] args = {
                startTime,              // ARGV[1]
                String.valueOf(maxPerSlot),  // ARGV[2]
                "86400",                // ARGV[3]
                userId.toString(),      // ARGV[4]
                "86400"                 // ARGV[5]
        };

        // 6.3. 执行脚本
        Long result;
        try {
            result = stringRedisTemplate.execute(bookingLuaScript, keys, args);
        } catch (Exception e) {
            log.error("Redis Lua 脚本执行异常", e);
            return Result.error("系统繁忙，请稍后重试");
        }

        if (result == null) {
            log.error("Lua 脚本返回 null，可能脚本路径错误或语法问题");
            return Result.error("系统错误");
        }

        if (result == 0) {
            return Result.error("您已预约过该时段");
        } else if (result == -1) {
            return Result.error("该时段已被约满");
        }

        // ================== 3. 落库入库 ==================
        BookingOrder order = new BookingOrder();
        order.setOrderNo(IdUtil.fastSimpleUUID());    // 订单号仍用 UUID，便于展示
        order.setUserId(userId);
        order.setResourceId(resourceId);
        order.setTenantId(resource.getTenantId());
        order.setBookingDate(LocalDate.parse(bookingDate));
        order.setStartTime(LocalTime.parse(startTime));
        order.setEndTime(LocalTime.parse(startTime).plusMinutes(configByResource.getSlotDuration()));
        order.setStatus(1);                           // 已确认

        try {
            orderMapper.insert(order);
        } catch (Exception e) {
            // ================== 4. 落库失败 → 回滚 Redis ==================
            log.error("订单入库失败，开始回滚 Redis 库存", e);
            try {
                // 回滚库存哈希
                stringRedisTemplate.opsForHash().increment(hashKey, startTime.toString(), -1);
                // 回滚用户预约记录集合
                stringRedisTemplate.opsForSet().remove(setKey, userId.toString());
            } catch (Exception rollbackEx) {
                log.error("Redis 回滚失败！需人工介入，hashKey={}, setKey={}, userId={}",
                        hashKey, setKey, userId, rollbackEx);
            }
            return Result.error("订单创建失败，请稍后重试");
        }

        // ================== 5. 成功返回 ==================
        OrderResultDTO dtoResult = new OrderResultDTO();
        dtoResult.setOrderNo(order.getOrderNo());
        dtoResult.setId(order.getId().toString());
        return Result.success("预约成功", dtoResult);

    }

    @Override
    public Result<PageResult<OrderPageQueryVO>> queryMyOrders(OrderPageQueryDTO queryDTO) {
        // 1.获取当前用户ID
        Long userId = UserHolder.getUser().getId();
        Page<OrderPageQueryVO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        List<OrderPageQueryVO> orderPageQueryVOS = orderMapper.selectOrderPage(page, userId, queryDTO.getStatus());
        PageResult<OrderPageQueryVO> pageResult = new PageResult<>();
        // 2.封装分页数据
        pageResult.setRecords(orderPageQueryVOS);
        pageResult.setCurrent(page.getCurrent());
        pageResult.setTotal(page.getTotal());
        pageResult.setSize(page.getSize());

        return Result.success(pageResult);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> cancelOrder(String id) {
        // 1.获取当前用户ID
        Long userId = UserHolder.getUser().getId();
        // 1.2 判断订单是否存在
        BookingOrder order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        // 1.3.判断当前用户是否是订单的创建者
        if (!order.getUserId().equals(userId)) {
            return Result.error("您没有权限取消该订单");
        }
        if (order.getStatus() != 1) {
            return Result.error("订单已完成或订单已取消");
        }
        // 2.判断当前时间是否已经超过预约开始时间
        // 2.1.查询改资源的cancel_minutes
        BookingConfig config = appointmentMapper.getConfigByResourceId(order.getResourceId());
        Integer cancelLimitMinutes = config.getCancelLimitMinutes();
        LocalDateTime bookingStartTime = LocalDateTime.of(order.getBookingDate(), order.getStartTime());
        if (LocalDateTime.now().isAfter(bookingStartTime.minusMinutes(cancelLimitMinutes))) {
            return Result.error("已超过取消时间");
        }
        //3.恢复 Redis 库存
        String hashKey = "slot:count:" + order.getResourceId() + ":" + order.getBookingDate();
        String setKey = "booking:user:" + order.getResourceId() + ":" + order.getBookingDate() + ":" + order.getStartTime();
       try {
           stringRedisTemplate.opsForHash().increment(hashKey, order.getStartTime().toString(), -1);
           stringRedisTemplate.opsForSet().remove(setKey, order.getUserId().toString());
       }catch (Exception e){
           log.error("取消订单时 Redis 恢复失败，orderId={}", id, e);
       }
        order.setStatus(3);
        orderMapper.updateById(order);
        return Result.success("取消成功");
    }

    @Override
    public Result<PageResult<OrderPageQueryVO>> queryAdminOrders(OrderPageQueryDTO queryDTO, Long resourceId) {
        Page<OrderPageQueryDTO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        List<OrderPageQueryVO> orderPage = orderMapper.selectAdminOrderPage(page, resourceId, queryDTO.getStatus());
        PageResult<OrderPageQueryVO> pageResult = new PageResult<>();
        pageResult.setRecords(orderPage);
        pageResult.setCurrent(page.getCurrent());
        pageResult.setTotal(page.getTotal());
        pageResult.setSize(page.getSize());
        return Result.success(pageResult);
    }
}
