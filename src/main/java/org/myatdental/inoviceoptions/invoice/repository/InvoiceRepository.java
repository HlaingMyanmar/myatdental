package org.myatdental.inoviceoptions.invoice.repository;


import org.myatdental.inoviceoptions.invoice.dto.InvoiceResponseDTO;
import org.myatdental.inoviceoptions.invoice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Optional<Invoice> findByCode(String code);
    List<Invoice> findByPatient_Id(Integer patientId);


    List<Invoice> findByStatus(Invoice.InvoiceStatus status);


}