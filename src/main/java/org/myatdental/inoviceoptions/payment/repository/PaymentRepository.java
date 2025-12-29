
package org.myatdental.inoviceoptions.payment.repository;
import org.myatdental.inoviceoptions.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByCode(String code);

    List<Payment> findByInvoice_InvoiceId(Integer invoiceId);

}