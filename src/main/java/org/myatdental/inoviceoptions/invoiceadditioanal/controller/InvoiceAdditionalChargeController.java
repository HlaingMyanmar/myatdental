package org.myatdental.inoviceoptions.invoiceadditioanal.controller;

import jakarta.validation.Valid;

import org.myatdental.inoviceoptions.invoiceadditioanal.dto.InvoiceAdditionalChargeRequestDTO;
import org.myatdental.inoviceoptions.invoiceadditioanal.dto.InvoiceAdditionalChargeResponseDTO;
import org.myatdental.inoviceoptions.invoiceadditioanal.service.InvoiceAdditionalChargeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-additional-charges")
public class InvoiceAdditionalChargeController {

    private final InvoiceAdditionalChargeService invoiceAdditionalChargeService;

    public InvoiceAdditionalChargeController(InvoiceAdditionalChargeService invoiceAdditionalChargeService) {
        this.invoiceAdditionalChargeService = invoiceAdditionalChargeService;
    }

    @PostMapping
    public ResponseEntity<InvoiceAdditionalChargeResponseDTO> createInvoiceAdditionalCharge(@Valid @RequestBody InvoiceAdditionalChargeRequestDTO requestDTO) {
        try {
            InvoiceAdditionalChargeResponseDTO createdCharge = invoiceAdditionalChargeService.createInvoiceAdditionalCharge(requestDTO);
            return new ResponseEntity<>(createdCharge, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Or CONFLICT if unique constraint fails
        }
    }

    @GetMapping
    public ResponseEntity<List<InvoiceAdditionalChargeResponseDTO>> getAllInvoiceAdditionalCharges() {
        List<InvoiceAdditionalChargeResponseDTO> charges = invoiceAdditionalChargeService.getAllInvoiceAdditionalCharges();
        return new ResponseEntity<>(charges, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceAdditionalChargeResponseDTO> getInvoiceAdditionalChargeById(@PathVariable Integer id) {
        try {
            InvoiceAdditionalChargeResponseDTO charge = invoiceAdditionalChargeService.getInvoiceAdditionalChargeById(id);
            return new ResponseEntity<>(charge, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-invoice/{invoiceId}")
    public ResponseEntity<List<InvoiceAdditionalChargeResponseDTO>> getInvoiceAdditionalChargesByInvoiceId(@PathVariable Integer invoiceId) {
        List<InvoiceAdditionalChargeResponseDTO> charges = invoiceAdditionalChargeService.getInvoiceAdditionalChargesByInvoiceId(invoiceId);
        if (charges.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(charges, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceAdditionalChargeResponseDTO> updateInvoiceAdditionalCharge(@PathVariable Integer id, @Valid @RequestBody InvoiceAdditionalChargeRequestDTO requestDTO) {
        try {
            InvoiceAdditionalChargeResponseDTO updatedCharge = invoiceAdditionalChargeService.updateInvoiceAdditionalCharge(id, requestDTO);
            return new ResponseEntity<>(updatedCharge, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Or NOT_FOUND / CONFLICT
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceAdditionalCharge(@PathVariable Integer id) {
        try {
            invoiceAdditionalChargeService.deleteInvoiceAdditionalCharge(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

     @DeleteMapping("/by-invoice/{invoiceId}/by-charge/{chargeId}")
     public ResponseEntity<Void> deleteInvoiceAdditionalChargeByCompositeKey(@PathVariable Integer invoiceId, @PathVariable Integer chargeId) {
         try {
             invoiceAdditionalChargeService.deleteInvoiceAdditionalCharge(invoiceId, chargeId);
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
         } catch (IllegalArgumentException e) {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
     }
}