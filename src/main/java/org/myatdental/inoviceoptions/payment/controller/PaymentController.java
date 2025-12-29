
package org.myatdental.inoviceoptions.payment.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.inoviceoptions.payment.dto.PaymentDTO;
import org.myatdental.inoviceoptions.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'RECEPTIONIST')")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'RECEPTIONIST')")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'RECEPTIONIST')")
    public ResponseEntity<PaymentDTO> getPaymentByCode(@PathVariable String code) {
        return ResponseEntity.ok(paymentService.getPaymentByCode(code));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'RECEPTIONIST')")
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO dto) {
        PaymentDTO createdPayment = paymentService.createPayment(dto);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'RECEPTIONIST')")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Integer id, @RequestBody PaymentDTO dto) {
        PaymentDTO updatedPayment = paymentService.updatePayment(id, dto);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')") // Typically only higher roles can delete
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}