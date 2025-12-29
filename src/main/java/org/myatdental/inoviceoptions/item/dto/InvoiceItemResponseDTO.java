package org.myatdental.inoviceoptions.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemResponseDTO {

    private Integer itemId;
    private Integer invoiceId;
    private String invoiceCode; // Added for convenience
    private Integer treatmentRecordId;
    private String treatmentRecordDescription; // Added for convenience
    private String itemDescription;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal; // Generated column
}