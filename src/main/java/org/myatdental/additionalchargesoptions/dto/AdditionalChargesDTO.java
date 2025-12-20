package org.myatdental.additionalchargesoptions.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AdditionalChargesDTO {

    private Integer chargeId;

    @NotBlank(message = "Charge name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String name;

    @Size(max = 500, message = "Description is too long")
    private String description;

    @NotNull(message = "Default price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Price format is invalid (up to 10 digits and 2 decimals)")
    private BigDecimal defaultPrice;

    private Boolean isActive;
}