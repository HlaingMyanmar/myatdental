package org.myatdental.inoviceoptions.item.service;


import org.myatdental.inoviceoptions.invoice.model.Invoice;
import org.myatdental.inoviceoptions.invoice.repository.InvoiceRepository;
import org.myatdental.inoviceoptions.item.dto.InvoiceItemRequestDTO;
import org.myatdental.inoviceoptions.item.dto.InvoiceItemResponseDTO;
import org.myatdental.inoviceoptions.item.model.InvoiceItem;
import org.myatdental.inoviceoptions.item.repository.InvoiceItemRepository;
import org.myatdental.treatmentrecord.model.TreatmentRecord;
import org.myatdental.treatmentrecord.repository.TreatmentRecordRepository; // Assuming this exists

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceItemService {

    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final TreatmentRecordRepository treatmentRecordRepository;

    public InvoiceItemService(InvoiceItemRepository invoiceItemRepository,
                              InvoiceRepository invoiceRepository,
                              TreatmentRecordRepository treatmentRecordRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceRepository = invoiceRepository;
        this.treatmentRecordRepository = treatmentRecordRepository;
    }

    // Helper to convert Entity to ResponseDTO
    private InvoiceItemResponseDTO convertToDto(InvoiceItem item) {
        InvoiceItemResponseDTO dto = new InvoiceItemResponseDTO();
        dto.setItemId(item.getItemId());
        dto.setInvoiceId(item.getInvoice().getInvoiceId());
        dto.setInvoiceCode(item.getInvoice().getCode());
        if (item.getTreatmentRecord() != null) {
            dto.setTreatmentRecordId(Math.toIntExact(item.getTreatmentRecord().getRecordId()));

        }

        dto.setItemDescription(item.getItemDescription());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }

    @Transactional
    public InvoiceItemResponseDTO createInvoiceItem(InvoiceItemRequestDTO requestDTO) {
        Invoice invoice = invoiceRepository.findById(requestDTO.getInvoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + requestDTO.getInvoiceId() + " not found."));

        TreatmentRecord treatmentRecord = null;
        if (requestDTO.getTreatmentRecordId() != null) {
            treatmentRecord = treatmentRecordRepository.findById(Long.valueOf(requestDTO.getTreatmentRecordId()))
                    .orElseThrow(() -> new IllegalArgumentException("Treatment Record with ID " + requestDTO.getTreatmentRecordId() + " not found."));
        }

        InvoiceItem item = new InvoiceItem();
        item.setInvoice(invoice);
        item.setTreatmentRecord(treatmentRecord);
        item.setItemDescription(requestDTO.getItemDescription());
        item.setQuantity(requestDTO.getQuantity() != null ? requestDTO.getQuantity() : 1);
        item.setUnitPrice(requestDTO.getUnitPrice());

        InvoiceItem savedItem = invoiceItemRepository.save(item);
        return convertToDto(savedItem);
    }

    @Transactional(readOnly = true)
    public List<InvoiceItemResponseDTO> getAllInvoiceItems() {
        return invoiceItemRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InvoiceItemResponseDTO getInvoiceItemById(Integer id) {
        return invoiceItemRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("Invoice Item with ID " + id + " not found."));
    }

    @Transactional(readOnly = true)
    public List<InvoiceItemResponseDTO> getInvoiceItemsByInvoiceId(Integer invoiceId) {
        return invoiceItemRepository.findByInvoice_InvoiceId(invoiceId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InvoiceItemResponseDTO updateInvoiceItem(Integer id, InvoiceItemRequestDTO requestDTO) {
        InvoiceItem existingItem = invoiceItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice Item with ID " + id + " not found."));

        Invoice invoice = invoiceRepository.findById(requestDTO.getInvoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + requestDTO.getInvoiceId() + " not found."));
        existingItem.setInvoice(invoice);

        TreatmentRecord treatmentRecord = null;
        if (requestDTO.getTreatmentRecordId() != null) {
            treatmentRecord = treatmentRecordRepository.findById(Long.valueOf(requestDTO.getTreatmentRecordId()))
                    .orElseThrow(() -> new IllegalArgumentException("Treatment Record with ID " + requestDTO.getTreatmentRecordId() + " not found."));
        }
        existingItem.setTreatmentRecord(treatmentRecord);

        existingItem.setItemDescription(requestDTO.getItemDescription());
        existingItem.setQuantity(requestDTO.getQuantity() != null ? requestDTO.getQuantity() : existingItem.getQuantity());
        existingItem.setUnitPrice(requestDTO.getUnitPrice());

        InvoiceItem updatedItem = invoiceItemRepository.save(existingItem);
        return convertToDto(updatedItem);
    }

    @Transactional
    public void deleteInvoiceItem(Integer id) {
        if (!invoiceItemRepository.existsById(id)) {
            throw new IllegalArgumentException("Invoice Item with ID " + id + " not found.");
        }
        invoiceItemRepository.deleteById(id);
    }
}