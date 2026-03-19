package com.flexreserve.appointment.mapper;

import com.flexreserve.appointment.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppointmentMapper {

   List<Appointment> selectByUserId (Long userId);
}
