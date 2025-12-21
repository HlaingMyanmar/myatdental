package org.myatdental.appointmentoptions.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Integer id;
    private String code;
    private Integer patientId;
    private Integer dentistId;
    private Integer roomId;
    private Integer tokenNumber;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Integer durationMinutes;
    private String status;
    private LocalDateTime checkInTime;
    private Integer patientPlanId;
    private Boolean isFollowUp;
    private Integer previousAppointmentId;
    private String notes;
}
