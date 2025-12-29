package org.myatdental.inoviceoptions.invoice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myatdental.inoviceoptions.invoice.model.Invoice;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequestDTO {

    @NotBlank(message = "Invoice code is required")
    @Size(max = 15, message = "Invoice code cannot exceed 15 characters")
    private String code;

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    private Integer patientPlanId;

    private LocalDate invoiceDate;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;

    @DecimalMin(value = "0.0", message = "Tax amount cannot be negative")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", message = "Discount amount cannot be negative")
    private BigDecimal discountAmount;

    @DecimalMin(value = "0.0", message = "Amount paid cannot be negative")
    private BigDecimal amountPaid;

    private Invoice.InvoiceStatus status;
}