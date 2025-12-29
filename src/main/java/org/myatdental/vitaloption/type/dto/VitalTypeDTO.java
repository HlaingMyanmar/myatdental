package org.myatdental.vitaloption.type.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VitalTypeDTO {
    private Integer typeId;

    @NotBlank(message = "Vital name cannot be blank")
    @Size(max = 50, message = "Vital name cannot exceed 50 characters")
    private String vitalName;

    @Size(max = 20, message = "Unit cannot exceed 20 characters")
    private String unit;

    private String description;
    private Boolean isActive;
}