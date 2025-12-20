package org.myatdental.treatmentplanoptions.plan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.myatdental.treatmentplanoptions.item.dto.TreatmentPlanItemDTO;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TreatmentPlanDTO {
    private Integer planId;
    @NotBlank(message = "Code is required")
    private String code;
    @NotBlank(message = "Plan name is required")
    private String name;
    private Boolean isTemplate;
    private BigDecimal totalCost;
    private Boolean installmentsAllowed;
    private Boolean isActive;
    private List<TreatmentPlanItemDTO> items;
}
