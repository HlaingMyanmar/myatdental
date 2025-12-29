package org.myatdental.inoviceoptions.invoiceadditioanal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceAdditionalChargeResponseDTO {

    private Integer itemId; // The PK for this association entity
    private Integer invoiceId;
    private String invoiceCode; // Added for convenience
    private Integer chargeId;
    private String chargeName; // Added for convenience, assuming AdditionalCharge has a name field
    private BigDecimal chargedAmount;
    private String chargeNotes;
}