package org.myatdental.inoviceoptions.invoiceadditioanal.service;


import org.myatdental.additionalchargesoptions.model.AdditionalCharges;
import org.myatdental.additionalchargesoptions.repository.AdditionalChargesRepository;
import org.myatdental.inoviceoptions.invoice.model.Invoice;
import org.myatdental.inoviceoptions.invoice.repository.InvoiceRepository;
import org.myatdental.inoviceoptions.invoiceadditioanal.dto.InvoiceAdditionalChargeRequestDTO;
import org.myatdental.inoviceoptions.invoiceadditioanal.dto.InvoiceAdditionalChargeResponseDTO;
import org.myatdental.inoviceoptions.invoiceadditioanal.model.InvoiceAdditionalCharge;
import org.myatdental.inoviceoptions.invoiceadditioanal.repository.InvoiceAdditionalChargeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceAdditionalChargeService {

    private final InvoiceAdditionalChargeRepository invoiceAdditionalChargeRepository;
    private final InvoiceRepository invoiceRepository;
    private final AdditionalChargesRepository additionalChargeRepository;

    public InvoiceAdditionalChargeService(InvoiceAdditionalChargeRepository invoiceAdditionalChargeRepository,
                                          InvoiceRepository invoiceRepository,
                                          AdditionalChargesRepository additionalChargeRepository) {
        this.invoiceAdditionalChargeRepository = invoiceAdditionalChargeRepository;
        this.invoiceRepository = invoiceRepository;
        this.additionalChargeRepository = additionalChargeRepository;
    }

    // Helper to convert Entity to ResponseDTO
    private InvoiceAdditionalChargeResponseDTO convertToDto(InvoiceAdditionalCharge entity) {
        InvoiceAdditionalChargeResponseDTO dto = new InvoiceAdditionalChargeResponseDTO();
        dto.setItemId(entity.getItemId());
        dto.setInvoiceId(entity.getInvoice().getInvoiceId());
        dto.setInvoiceCode(entity.getInvoice().getCode()); // Assuming Invoice has getCode()
        dto.setChargeId(entity.getCharge().getChargeId()); // Assuming AdditionalCharge has getChargeId()
        dto.setChargeName(entity.getCharge().getName());   // Assuming AdditionalCharge has getName()
        dto.setChargedAmount(entity.getChargedAmount());
        dto.setChargeNotes(entity.getChargeNotes());
        return dto;
    }

    @Transactional
    public InvoiceAdditionalChargeResponseDTO createInvoiceAdditionalCharge(InvoiceAdditionalChargeRequestDTO requestDTO) {
        Invoice invoice = invoiceRepository.findById(requestDTO.getInvoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + requestDTO.getInvoiceId() + " not found."));

        AdditionalCharges charge = additionalChargeRepository.findById(Long.valueOf(requestDTO.getChargeId()))
                .orElseThrow(() -> new IllegalArgumentException("Additional Charge with ID " + requestDTO.getChargeId() + " not found."));

        // Check for uniqueness constraint: invoice_id, charge_id must be unique
        if (invoiceAdditionalChargeRepository.findByInvoice_InvoiceIdAndCharge_ChargeId(invoice.getInvoiceId(), charge.getChargeId()).isPresent()) {
            throw new IllegalArgumentException("Additional charge '" + charge.getName() + "' already added to Invoice ID " + invoice.getInvoiceId());
        }

        InvoiceAdditionalCharge entity = new InvoiceAdditionalCharge();
        entity.setInvoice(invoice);
        entity.setCharge(charge);
        entity.setChargedAmount(requestDTO.getChargedAmount());
        entity.setChargeNotes(requestDTO.getChargeNotes());

        InvoiceAdditionalCharge savedEntity = invoiceAdditionalChargeRepository.save(entity);
        return convertToDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<InvoiceAdditionalChargeResponseDTO> getAllInvoiceAdditionalCharges() {
        return invoiceAdditionalChargeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InvoiceAdditionalChargeResponseDTO getInvoiceAdditionalChargeById(Integer id) {
        return invoiceAdditionalChargeRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("Invoice Additional Charge with ID " + id + " not found."));
    }

    @Transactional(readOnly = true)
    public List<InvoiceAdditionalChargeResponseDTO> getInvoiceAdditionalChargesByInvoiceId(Integer invoiceId) {
        return invoiceAdditionalChargeRepository.findByInvoice_InvoiceId(invoiceId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InvoiceAdditionalChargeResponseDTO updateInvoiceAdditionalCharge(Integer id, InvoiceAdditionalChargeRequestDTO requestDTO) {
        InvoiceAdditionalCharge existingEntity = invoiceAdditionalChargeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice Additional Charge with ID " + id + " not found."));

        Invoice invoice = invoiceRepository.findById(requestDTO.getInvoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + requestDTO.getInvoiceId() + " not found."));
        existingEntity.setInvoice(invoice); // Can update parent invoice if needed

        AdditionalCharges charge = additionalChargeRepository.findById(Long.valueOf(requestDTO.getChargeId()))
                .orElseThrow(() -> new IllegalArgumentException("Additional Charge with ID " + requestDTO.getChargeId() + " not found."));

        // Check uniqueness if charge_id is changed
        if (!existingEntity.getCharge().getChargeId().equals(charge.getChargeId())) {
            if (invoiceAdditionalChargeRepository.findByInvoice_InvoiceIdAndCharge_ChargeId(invoice.getInvoiceId(), charge.getChargeId()).isPresent()) {
                throw new IllegalArgumentException("Additional charge '" + charge.getName() + "' already added to Invoice ID " + invoice.getInvoiceId());
            }
        }
        existingEntity.setCharge(charge);

        existingEntity.setChargedAmount(requestDTO.getChargedAmount());
        existingEntity.setChargeNotes(requestDTO.getChargeNotes());

        InvoiceAdditionalCharge updatedEntity = invoiceAdditionalChargeRepository.save(existingEntity);
        return convertToDto(updatedEntity);
    }

    @Transactional
    public void deleteInvoiceAdditionalCharge(Integer id) {
        if (!invoiceAdditionalChargeRepository.existsById(id)) {
            throw new IllegalArgumentException("Invoice Additional Charge with ID " + id + " not found.");
        }
        invoiceAdditionalChargeRepository.deleteById(id);
    }

    // Optional: Delete by composite key (if frontend needs it)
    @Transactional
    public void deleteInvoiceAdditionalCharge(Integer invoiceId, Integer chargeId) {
        if (!invoiceAdditionalChargeRepository.findByInvoice_InvoiceIdAndCharge_ChargeId(invoiceId, chargeId).isPresent()) {
            throw new IllegalArgumentException("Invoice Additional Charge not found for Invoice ID " + invoiceId + " and Charge ID " + chargeId);
        }
        invoiceAdditionalChargeRepository.deleteByInvoice_InvoiceIdAndCharge_ChargeId(invoiceId, chargeId);
    }
}