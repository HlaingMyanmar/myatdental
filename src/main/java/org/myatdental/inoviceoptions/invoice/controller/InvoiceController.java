package org.myatdental.inoviceoptions.invoice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.inoviceoptions.invoice.dto.InvoiceRequestDTO;
import org.myatdental.inoviceoptions.invoice.dto.InvoiceResponseDTO;
import org.myatdental.inoviceoptions.invoice.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    private final SimpMessagingTemplate messagingTemplate;

    private static final String CHARGE_TOPIC = "/topic/invoices";


    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@Valid @RequestBody InvoiceRequestDTO requestDTO) {
        try {
            InvoiceResponseDTO createdInvoice = invoiceService.createInvoice(requestDTO);
            messagingTemplate.convertAndSend(CHARGE_TOPIC,"CREATED_INVOICE");
            return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'RECEPTIONIST')")
    public ResponseEntity<InvoiceResponseDTO> cancelInvoice(
            @PathVariable Integer id,
            @Valid @RequestBody InvoiceResponseDTO requestDTO) {
        try {

            InvoiceResponseDTO cancelledInvoice = invoiceService.cancelInvoice(id, requestDTO.getCancellationReason());
            messagingTemplate.convertAndSend(CHARGE_TOPIC,"CANCEL_INVOICE");
            return new ResponseEntity<>(cancelledInvoice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponseDTO>> getAllInvoices() {
        List<InvoiceResponseDTO> invoices = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable Integer id) {
        try {
            InvoiceResponseDTO invoice = invoiceService.getInvoiceById(id);
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<InvoiceResponseDTO>> getInvoicesByPatientId(@PathVariable Integer patientId) {
        List<InvoiceResponseDTO> invoices = invoiceService.getInvoicesByPatientId(patientId);
        if (invoices.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> updateInvoice(@PathVariable Integer id, @Valid @RequestBody InvoiceRequestDTO requestDTO) {
        try {
            InvoiceResponseDTO updatedInvoice = invoiceService.updateInvoice(id, requestDTO);
            messagingTemplate.convertAndSend(CHARGE_TOPIC,"UPDATE_INVOICE");
            return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Integer id) {
        try {
            invoiceService.deleteInvoice(id);
            messagingTemplate.convertAndSend(CHARGE_TOPIC,"DELETE_INVOICE");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // New API endpoint to get invoices by status (generic)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InvoiceResponseDTO>> getInvoicesByStatus(@PathVariable String status) {
        try {
            List<InvoiceResponseDTO> invoices = invoiceService.getInvoicesByStatus(status);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Log the exception for debugging
            System.err.println("Error getting invoices by status: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid status provided
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<InvoiceResponseDTO>> getPendingInvoices() {
        List<InvoiceResponseDTO> invoices = invoiceService.getPendingInvoices();
        if (invoices.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/paid")
    public ResponseEntity<List<InvoiceResponseDTO>> getPaidInvoices() {
        List<InvoiceResponseDTO> invoices = invoiceService.getPaidInvoices();
        if (invoices.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/partial")
    public ResponseEntity<List<InvoiceResponseDTO>> getPartialInvoices() {
        List<InvoiceResponseDTO> invoices = invoiceService.getPartialInvoices();
        if (invoices.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/canceled")
    public ResponseEntity<List<InvoiceResponseDTO>> getCanceledInvoices() {
        List<InvoiceResponseDTO> invoices = invoiceService.getCanceledInvoices();
        if (invoices.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

}