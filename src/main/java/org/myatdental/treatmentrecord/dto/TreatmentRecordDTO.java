package org.myatdental.treatmentrecord.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TreatmentRecordDTO {

    private Long recordId;


    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;


    @NotNull(message = "Treatment ID is required")
    private Long treatmentId;

    @Size(max = 50, message = "Tooth or site must not exceed 50 characters")
    private String toothOrSite;

    private String diagnosis;

    @NotNull(message = "Price charged is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be positive")
    private BigDecimal priceCharged;

    private String recordNotes;

    private LocalDateTime performedAt;
}

