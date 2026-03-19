package com.flexreserve.appointment.service;

import com.flexreserve.appointment.entity.Appointment;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AppointmentService {
    List<Appointment> getAppointmentsByUserId(Long userId);
}
