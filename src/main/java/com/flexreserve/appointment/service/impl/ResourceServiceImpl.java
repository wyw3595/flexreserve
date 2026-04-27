package com.flexreserve.appointment.service.impl;

import com.flexreserve.appointment.entity.vo.Resource;
import com.flexreserve.appointment.mapper.ResourceMapper;
import com.flexreserve.appointment.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceMapper resourceMapper;
    @Override
    public List<Resource> getResourceList() {
        return resourceMapper.getResourceList();
    }
}
