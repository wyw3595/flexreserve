package com.flexreserve.appointment.mapper;

import com.flexreserve.appointment.entity.vo.Resource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceMapper {
    List<Resource> getResourceList();
}
