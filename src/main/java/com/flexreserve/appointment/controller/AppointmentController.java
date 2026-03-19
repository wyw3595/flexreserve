package com.flexreserve.appointment.controller;

import com.flexreserve.appointment.entity.Appointment;
import com.flexreserve.appointment.service.AppointmentService;
import com.flexreserve.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private  final AppointmentService appointmentService;

    @GetMapping("/user/{userId}")
    public Result<List<Appointment>> getAppointmentsByUserId(@PathVariable Long userId) {
        List<Appointment> appointmentsByUserId = appointmentService.getAppointmentsByUserId(userId);
        return Result.success(appointmentsByUserId);
    }
}
