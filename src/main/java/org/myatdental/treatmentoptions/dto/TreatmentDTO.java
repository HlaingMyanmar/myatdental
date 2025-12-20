package org.myatdental.treatmentoptions.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TreatmentDTO {

    private Integer treatment_id;

    @NotBlank(message = "Treatment code is required")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String code;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Standard price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be positive")
    private BigDecimal standard_price;

    @NotNull(message = "Category is required")
    private Integer category_id;  // This will hold the ID of TreatmentCategory

    private Boolean isActive = true;
}
