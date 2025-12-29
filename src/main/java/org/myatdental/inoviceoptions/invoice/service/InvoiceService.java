package org.myatdental.inoviceoptions.invoice.service;


import org.myatdental.inoviceoptions.invoice.dto.InvoiceRequestDTO;
import org.myatdental.inoviceoptions.invoice.dto.InvoiceResponseDTO;
import org.myatdental.inoviceoptions.invoice.model.Invoice;
import org.myatdental.inoviceoptions.invoice.repository.InvoiceRepository;
import org.myatdental.inoviceoptions.invoiceadditioanal.dto.InvoiceAdditionalChargeResponseDTO;
import org.myatdental.inoviceoptions.invoiceadditioanal.service.InvoiceAdditionalChargeService;
import org.myatdental.inoviceoptions.item.dto.InvoiceItemResponseDTO;
import org.myatdental.inoviceoptions.item.service.InvoiceItemService;
import org.myatdental.inoviceoptions.payment.model.Payment;
import org.myatdental.inoviceoptions.payment.repository.PaymentRepository;
import org.myatdental.inoviceoptions.payment.service.PaymentService;
import org.myatdental.patientoption.model.Patient;

import org.myatdental.patientoption.respository.PatientRepository;
import org.myatdental.patientplanoption.model.PatientPlan;
import org.myatdental.patientplanoption.respository.PatientPlanRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;
    private final PatientPlanRepository patientPlanRepository;
    private final InvoiceItemService invoiceItemService;
    private final InvoiceAdditionalChargeService invoiceAdditionalChargeService;
    private final PaymentRepository paymentRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, PatientRepository patientRepository, PatientPlanRepository patientPlanRepository, InvoiceItemService invoiceItemService, InvoiceAdditionalChargeService invoiceAdditionalChargeService, PaymentRepository paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.patientRepository = patientRepository;
        this.patientPlanRepository = patientPlanRepository;
        this.invoiceItemService = invoiceItemService;
        this.invoiceAdditionalChargeService = invoiceAdditionalChargeService;
        this.paymentRepository = paymentRepository;
    }

    // Helper to convert Entity to ResponseDTO
    private InvoiceResponseDTO convertToDto(Invoice invoice) {
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setCode(invoice.getCode());
        dto.setPatientId(invoice.getPatient().getId());
        dto.setPatientName(invoice.getPatient().getName());

        if (invoice.getPatientPlan() != null) {
            dto.setPatientPlanId(invoice.getPatientPlan().getId());
            dto.setPatientPlanName(invoice.getPatientPlan().getPlan().getName()); }

        dto.setInvoiceDate(invoice.getInvoiceDate());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setTaxAmount(invoice.getTaxAmount());
        dto.setDiscountAmount(invoice.getDiscountAmount());
        dto.setAmountPaid(invoice.getAmountPaid());
        dto.setBalanceDue(invoice.getBalanceDue());
        dto.setStatus(invoice.getStatus());
        dto.setCreatedAt(invoice.getCreatedAt());
        return dto;
    }

    @Transactional
    public InvoiceResponseDTO createInvoice(InvoiceRequestDTO requestDTO) {
        if (invoiceRepository.findByCode(requestDTO.getCode()).isPresent()) {
            throw new IllegalArgumentException("Invoice with code " + requestDTO.getCode() + " already exists.");
        }

        Patient patient = patientRepository.findById(requestDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient with ID " + requestDTO.getPatientId() + " not found."));

        PatientPlan patientPlan = null;
        if (requestDTO.getPatientPlanId() != null) {
            patientPlan = patientPlanRepository.findById(Long.valueOf(requestDTO.getPatientId()))
                    .orElseThrow(() -> new IllegalArgumentException("Patient Plan with ID " + requestDTO.getPatientPlanId() + " not found."));
        }

        Invoice invoice = new Invoice();
        invoice.setCode(requestDTO.getCode());
        invoice.setPatient(patient);
        invoice.setPatientPlan(patientPlan);
        invoice.setInvoiceDate(requestDTO.getInvoiceDate() != null ? requestDTO.getInvoiceDate() : LocalDate.now());
        invoice.setTotalAmount(requestDTO.getTotalAmount());
        invoice.setTaxAmount(requestDTO.getTaxAmount() != null ? requestDTO.getTaxAmount() : BigDecimal.ZERO);
        invoice.setDiscountAmount(requestDTO.getDiscountAmount() != null ? requestDTO.getDiscountAmount() : BigDecimal.ZERO);
        invoice.setAmountPaid(requestDTO.getAmountPaid() != null ? requestDTO.getAmountPaid() : BigDecimal.ZERO);
        invoice.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : Invoice.InvoiceStatus.PENDING);
        // createdAt is set by @PrePersist

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToDto(savedInvoice);
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InvoiceResponseDTO getInvoiceById(Integer id) {
        return invoiceRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + id + " not found."));
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> getInvoicesByPatientId(Integer patientId) {
        return invoiceRepository.findByPatient_Id(patientId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public InvoiceResponseDTO updateInvoice(Integer id, InvoiceRequestDTO requestDTO) {
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + id + " not found."));

        // Check if code is changed and if new code already exists
        if (!existingInvoice.getCode().equals(requestDTO.getCode()) && invoiceRepository.findByCode(requestDTO.getCode()).isPresent()) {
            throw new IllegalArgumentException("Invoice with code " + requestDTO.getCode() + " already exists.");
        }
        existingInvoice.setCode(requestDTO.getCode());

        Patient patient = patientRepository.findById(requestDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient with ID " + requestDTO.getPatientId() + " not found."));
        existingInvoice.setPatient(patient);

        PatientPlan patientPlan = null;
        if (requestDTO.getPatientPlanId() != null) {
            patientPlan = patientPlanRepository.findById(Long.valueOf(requestDTO.getPatientPlanId()))
                    .orElseThrow(() -> new IllegalArgumentException("Patient Plan with ID " + requestDTO.getPatientPlanId() + " not found."));
        }
        existingInvoice.setPatientPlan(patientPlan);

        existingInvoice.setInvoiceDate(requestDTO.getInvoiceDate() != null ? requestDTO.getInvoiceDate() : existingInvoice.getInvoiceDate());
        existingInvoice.setTotalAmount(requestDTO.getTotalAmount());
        existingInvoice.setTaxAmount(requestDTO.getTaxAmount() != null ? requestDTO.getTaxAmount() : BigDecimal.ZERO);
        existingInvoice.setDiscountAmount(requestDTO.getDiscountAmount() != null ? requestDTO.getDiscountAmount() : BigDecimal.ZERO);
        existingInvoice.setAmountPaid(requestDTO.getAmountPaid() != null ? requestDTO.getAmountPaid() : BigDecimal.ZERO);
        existingInvoice.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : existingInvoice.getStatus());

        Invoice updatedInvoice = invoiceRepository.save(existingInvoice);
        return convertToDto(updatedInvoice);
    }

    @Transactional
    public void deleteInvoice(Integer id) {
        if (!invoiceRepository.existsById(id)) {
            throw new IllegalArgumentException("Invoice with ID " + id + " not found.");
        }
        invoiceRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> getInvoicesByStatus(String status) {
        try {
            Invoice.InvoiceStatus invoiceStatus = Invoice.InvoiceStatus.valueOf(status.toUpperCase());
            return invoiceRepository.findByStatus(invoiceStatus).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid invoice status: " + status + ". Valid statuses are PENDING, PAID, PARTIAL, CANCELED.");
        }
    }
    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> getPendingInvoices() {
        return invoiceRepository.findByStatus(Invoice.InvoiceStatus.PENDING).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> getPaidInvoices() {
        return invoiceRepository.findByStatus(Invoice.InvoiceStatus.PAID).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> getPartialInvoices() {
        return invoiceRepository.findByStatus(Invoice.InvoiceStatus.PARTIAL).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> getCanceledInvoices() {
        return invoiceRepository.findByStatus(Invoice.InvoiceStatus.CANCELED).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalDueAmountForInvoice(Integer invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));

        BigDecimal totalItemsAmount = BigDecimal.ZERO;
        BigDecimal totalAdditionalChargesAmount = BigDecimal.ZERO;
        BigDecimal totalPaidAmount = BigDecimal.ZERO;


        List<InvoiceItemResponseDTO> items = invoiceItemService.getInvoiceItemsByInvoiceId(invoiceId);
        for (InvoiceItemResponseDTO item : items) {

            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalItemsAmount = totalItemsAmount.add(itemTotal);
        }
        List<InvoiceAdditionalChargeResponseDTO> charges = invoiceAdditionalChargeService.getInvoiceAdditionalChargesByInvoiceId(invoiceId);
        for (InvoiceAdditionalChargeResponseDTO charge : charges) {
            totalAdditionalChargesAmount = totalAdditionalChargesAmount.add(charge.getChargedAmount());
        }

        List<Payment> payments = paymentRepository.findByInvoice_InvoiceId(invoiceId);
        for (Payment payment : payments) {
            totalPaidAmount = totalPaidAmount.add(payment.getAmount());
        }

        BigDecimal grandTotal = totalItemsAmount.add(totalAdditionalChargesAmount);
        BigDecimal dueAmount = grandTotal.subtract(totalPaidAmount);

        return dueAmount;
    }
    @Transactional
    public InvoiceResponseDTO updateInvoiceStatus(Integer invoiceId, Invoice.InvoiceStatus newStatus) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));

        invoice.setStatus(newStatus);
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return convertToResponseDTO(updatedInvoice);
    }

    private InvoiceResponseDTO convertToResponseDTO(Invoice invoice) {
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        BeanUtils.copyProperties(invoice, dto);
        return dto;
    }


}