package org.myatdental.inoviceoptions.invoiceadditioanal.repository;

import org.myatdental.inoviceoptions.invoiceadditioanal.model.InvoiceAdditionalCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceAdditionalChargeRepository extends JpaRepository<InvoiceAdditionalCharge, Integer> {
    List<InvoiceAdditionalCharge> findByInvoice_InvoiceId(Integer invoiceId);

    // To check for uniqueness constraint (invoice_id, charge_id)
    Optional<InvoiceAdditionalCharge> findByInvoice_InvoiceIdAndCharge_ChargeId(Integer invoiceId, Integer chargeId);

    // You might also want to delete by invoice and charge ID
    void deleteByInvoice_InvoiceIdAndCharge_ChargeId(Integer invoiceId, Integer chargeId);
}