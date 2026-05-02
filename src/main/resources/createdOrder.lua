-- KEYS[1]: 库存 Hash 的 Key  "slot:count:{resourceId}:{date}"
-- KEYS[2]: 用户 SET 的 Key   "booking:user:{resourceId}:{date}:{startTime}"
-- ARGV[1]: 时段 field 名     "09:00"
-- ARGV[2]: 库存上限
-- ARGV[3]: 库存过期时间
-- ARGV[4]: 用户ID
-- ARGV[5]: 用户过期时间

-- 1. 检查是否已经预约过
local isBooked = redis.call('SISMEMBER', KEYS[2], ARGV[4])
if isBooked == 1 then
    return 0  -- 已重复预约
end

-- 2. 扣减库存
redis.call('HINCRBY', KEYS[1], ARGV[1], 1)
local current = redis.call('HGET', KEYS[1], ARGV[1])
local max = tonumber(ARGV[2])
redis.call('EXPIRE', KEYS[1], ARGV[3])

if tonumber(current) > max then
    -- 约满了，库存回滚，返回错误
    redis.call('HINCRBY', KEYS[1], ARGV[1], -1)
    return -1  -- 已满
end

-- 3. 标记该用户已经预约
redis.call('SADD', KEYS[2], ARGV[4])
redis.call('EXPIRE', KEYS[2], ARGV[5])

return 1  -- 成功