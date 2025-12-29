package org.myatdental.inoviceoptions.item.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequestDTO {

    @NotNull(message = "Invoice ID is required")
    private Integer invoiceId;

    private Integer treatmentRecordId; // Optional

    @NotBlank(message = "Item description is required")
    @Size(max = 255, message = "Item description cannot exceed 255 characters")
    private String itemDescription;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity; // Optional, defaults to 1

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;
}