
package org.myatdental.inoviceoptions.payment.service;
import lombok.RequiredArgsConstructor;
import org.myatdental.inoviceoptions.invoice.model.Invoice;
import org.myatdental.inoviceoptions.invoice.repository.InvoiceRepository;
import org.myatdental.inoviceoptions.invoice.service.InvoiceService;
import org.myatdental.inoviceoptions.payment.dto.PaymentDTO;
import org.myatdental.inoviceoptions.payment.model.Payment;
import org.myatdental.inoviceoptions.payment.repository.PaymentRepository;
import org.myatdental.paymentmethodoptions.model.PaymentMethods;
import org.myatdental.paymentmethodoptions.respositry.PaymentMethodRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final InvoiceService invoiceService;

    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return convertToDTO(payment);
    }

    @Transactional(readOnly = true)
    public PaymentDTO getPaymentByCode(String code) {
        Payment payment = paymentRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Payment not found with code: " + code));
        return convertToDTO(payment);
    }

    @Transactional
    public PaymentDTO createPayment(PaymentDTO dto) {
        Payment payment = new Payment();
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            dto.setCode("PAY-" + System.currentTimeMillis() % 1_000_000);
        } else {
            if (paymentRepository.findByCode(dto.getCode()).isPresent()) {
                throw new RuntimeException("Payment with code '" + dto.getCode() + "' already exists.");
            }
        }
        BigDecimal totalDueAmount = invoiceService.calculateTotalDueAmountForInvoice(dto.getInvoiceId());
        if (dto.getAmount().compareTo(totalDueAmount) > 0) {
            throw new RuntimeException("ပေးချေမှုပမာဏ (" + dto.getAmount() + ") သည် ပြေစာ၏ ကျန်ငွေပမာဏ (" + totalDueAmount + ") ထက် ပိုများနေ၍မရပါ။");
        }
        mapDtoToEntity(dto, payment);
        Payment savedPayment = paymentRepository.save(payment);
        BigDecimal updatedDueAmount = invoiceService.calculateTotalDueAmountForInvoice(dto.getInvoiceId());

        if (updatedDueAmount.compareTo(BigDecimal.ZERO) == 0) {

            invoiceService.updateInvoiceStatus(dto.getInvoiceId(), Invoice.InvoiceStatus.PAID);
        } else if (updatedDueAmount.compareTo(BigDecimal.ZERO) > 0) {

            Invoice currentInvoice = invoiceRepository.findById(dto.getInvoiceId())
                    .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + dto.getInvoiceId()));
            if (currentInvoice.getStatus() == Invoice.InvoiceStatus.PARTIAL) {
                invoiceService.updateInvoiceStatus(dto.getInvoiceId(), Invoice.InvoiceStatus.PARTIAL);
            }

        }
        return convertToDTO(savedPayment);
    }

    @Transactional
    public PaymentDTO updatePayment(Integer id, PaymentDTO dto) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        // If code is being updated, ensure it's unique (and not the same as an existing different payment)
        if (dto.getCode() != null && !dto.getCode().equals(existingPayment.getCode())) {
            if (paymentRepository.findByCode(dto.getCode()).isPresent()) {
                throw new RuntimeException("Payment with code '" + dto.getCode() + "' already exists.");
            }
        }

        mapDtoToEntity(dto, existingPayment); // Map DTO to entity
        // Ensure paymentDate is not reset if not provided in DTO
        if (dto.getPaymentDate() != null) {
            existingPayment.setPaymentDate(dto.getPaymentDate());
        }

        Payment updatedPayment = paymentRepository.save(existingPayment);
        return convertToDTO(updatedPayment);
    }

    @Transactional
    public void deletePayment(Integer id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    // --- Helper Methods ---

    private void mapDtoToEntity(PaymentDTO dto, Payment payment) {

        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + dto.getInvoiceId()));
        payment.setInvoice(invoice);


        PaymentMethods paymentMethod = paymentMethodRepository.findById(dto.getMethodId())
                .orElseThrow(() -> new RuntimeException("Payment Method not found with id: " + dto.getMethodId()));
        payment.setMethod(paymentMethod);


        payment.setCode(dto.getCode());
        payment.setAmount(dto.getAmount());
        payment.setNotes(dto.getNotes());
        if (dto.getPaymentDate() != null) {
            payment.setPaymentDate(dto.getPaymentDate());
        }
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        BeanUtils.copyProperties(payment, dto); // Copies matching fields

        // Manually set related IDs and details
        if (payment.getInvoice() != null) {
            dto.setInvoiceId(payment.getInvoice().getInvoiceId());
            dto.setInvoiceCode(payment.getInvoice().getCode()); // Set invoice code for DTO
        }
        if (payment.getMethod() != null) {
            dto.setMethodId(payment.getMethod().getId());
            dto.setMethodName(payment.getMethod().getName()); // Set method name for DTO
        }
        return dto;
    }
}