package com.flexreserve.appointment.service.impl;

import com.flexreserve.appointment.entity.Appointment;
import com.flexreserve.appointment.mapper.AppointmentMapper;
import com.flexreserve.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    @Override
    public List<Appointment> getAppointmentsByUserId(Long userId) {
     return appointmentMapper.selectByUserId(userId);
    }
}
