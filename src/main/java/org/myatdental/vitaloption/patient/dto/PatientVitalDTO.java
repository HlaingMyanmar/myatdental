package org.myatdental.vitaloption.patient.dto; // သင့် package အမှန်ကိုသုံးပါ။

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatientVitalDTO {
    private Integer readingId;

    @NotNull(message = "Patient ID cannot be null")
    private Integer patientId;

    @NotNull(message = "Appointment ID cannot be null")
    private Integer appointmentId;

    @NotNull(message = "Vital Type ID cannot be null")
    private Integer typeId;

    @NotBlank(message = "Vital value cannot be blank")
    @Size(max = 50, message = "Vital value cannot exceed 50 characters")
    private String vitalValue;

    private LocalDateTime recordedAt;
    private Long recordedById;
    private String vitalTypeName;
    private String vitalTypeUnit;
    private String recordedByName;
}