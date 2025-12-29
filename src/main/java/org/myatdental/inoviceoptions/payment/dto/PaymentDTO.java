
package org.myatdental.inoviceoptions.payment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Integer paymentId;
    private String code;
    private Integer invoiceId;
    private Integer methodId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String invoiceCode;
    private String methodName;
    private String notes;
}