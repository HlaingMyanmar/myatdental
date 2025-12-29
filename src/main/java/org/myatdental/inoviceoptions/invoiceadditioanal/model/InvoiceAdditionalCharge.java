package org.myatdental.inoviceoptions.invoiceadditioanal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myatdental.additionalchargesoptions.model.AdditionalCharges;
import org.myatdental.inoviceoptions.invoice.model.Invoice;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(
        name = "invoice_additional_charges",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_invoice_charge", columnNames = {"invoice_id", "charge_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceAdditionalCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id") // Note: The schema uses 'item_id' for this table's PK
    private Integer itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_id", nullable = false)
    private AdditionalCharges charge; // Link to the AdditionalCharge definition

    @Column(name = "charged_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal chargedAmount;

    @Column(name = "charge_notes", columnDefinition = "TEXT")
    private String chargeNotes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceAdditionalCharge that = (InvoiceAdditionalCharge) o;
        return Objects.equals(invoice, that.invoice) &&
               Objects.equals(charge, that.charge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoice, charge);
    }
}