package org.myatdental.patientplanoption.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PatientPlanDTO {
//
    private Long id;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Treatment Plan ID is required")
    private Long planId;

    private BigDecimal actualTotalCost;

    private LocalDate assignedDate;
}

