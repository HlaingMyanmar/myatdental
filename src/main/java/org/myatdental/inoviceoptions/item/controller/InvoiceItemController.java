package org.myatdental.inoviceoptions.item.controller;

import jakarta.validation.Valid;

import org.myatdental.inoviceoptions.item.dto.InvoiceItemRequestDTO;
import org.myatdental.inoviceoptions.item.dto.InvoiceItemResponseDTO;
import org.myatdental.inoviceoptions.item.service.InvoiceItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-items")
public class InvoiceItemController {

    private final InvoiceItemService invoiceItemService;

    public InvoiceItemController(InvoiceItemService invoiceItemService) {
        this.invoiceItemService = invoiceItemService;
    }

    @PostMapping
    public ResponseEntity<InvoiceItemResponseDTO> createInvoiceItem(@Valid @RequestBody InvoiceItemRequestDTO requestDTO) {
        try {
            InvoiceItemResponseDTO createdItem = invoiceItemService.createInvoiceItem(requestDTO);
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<InvoiceItemResponseDTO>> getAllInvoiceItems() {
        List<InvoiceItemResponseDTO> items = invoiceItemService.getAllInvoiceItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceItemResponseDTO> getInvoiceItemById(@PathVariable Integer id) {
        try {
            InvoiceItemResponseDTO item = invoiceItemService.getInvoiceItemById(id);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-invoice/{invoiceId}")
    public ResponseEntity<List<InvoiceItemResponseDTO>> getInvoiceItemsByInvoiceId(@PathVariable Integer invoiceId) {
        List<InvoiceItemResponseDTO> items = invoiceItemService.getInvoiceItemsByInvoiceId(invoiceId);
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceItemResponseDTO> updateInvoiceItem(@PathVariable Integer id, @Valid @RequestBody InvoiceItemRequestDTO requestDTO) {
        try {
            InvoiceItemResponseDTO updatedItem = invoiceItemService.updateInvoiceItem(id, requestDTO);
            return new ResponseEntity<>(updatedItem, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceItem(@PathVariable Integer id) {
        try {
            invoiceItemService.deleteInvoiceItem(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}