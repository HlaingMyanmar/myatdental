package org.myatdental.inoviceoptions.invoiceadditioanal.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceAdditionalChargeRequestDTO {

    @NotNull(message = "Invoice ID is required")
    private Integer invoiceId;

    @NotNull(message = "Charge ID is required")
    private Integer chargeId;

    @NotNull(message = "Charged amount is required")
    @DecimalMin(value = "0.0", message = "Charged amount cannot be negative")
    private BigDecimal chargedAmount;

    @Size(max = 255, message = "Charge notes cannot exceed 255 characters")
    private String chargeNotes;
}