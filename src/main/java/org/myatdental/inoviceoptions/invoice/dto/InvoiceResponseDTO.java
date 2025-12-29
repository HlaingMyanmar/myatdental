package org.myatdental.inoviceoptions.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myatdental.inoviceoptions.invoice.model.Invoice;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDTO {

    private Integer invoiceId;
    private String code;
    private Integer patientId;
    private String patientName;
    private Integer patientPlanId;
    private String patientPlanName;
    private LocalDate invoiceDate;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal amountPaid;
    private BigDecimal balanceDue;
    private Invoice.InvoiceStatus status;
    private LocalDateTime createdAt;
}