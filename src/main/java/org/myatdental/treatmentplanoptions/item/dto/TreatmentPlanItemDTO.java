package org.myatdental.treatmentplanoptions.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TreatmentPlanItemDTO {
    private Integer itemId;
    @NotNull(message = "Treatment ID is required")
    private Integer treatmentId;
    private String treatmentName;
    @NotNull(message = "Price is required")
    private BigDecimal estimatedPrice;
}
