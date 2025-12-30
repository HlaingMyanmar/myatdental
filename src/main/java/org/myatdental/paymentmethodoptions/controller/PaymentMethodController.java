package org.myatdental.paymentmethodoptions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.paymentmethodoptions.dto.PaymentMethodDTO;
import org.myatdental.paymentmethodoptions.service.PaymentMethodService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    private final SimpMessagingTemplate messagingTemplate;

    private static final String PAYMENT_METHOD_TOPIC = "/topic/payment_method";

    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> getAllPaymentMethods() {
        return ResponseEntity.ok(paymentMethodService.getAllPaymentMethods());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethodById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentMethodService.getPaymentMethodById(id));
    }

    @PostMapping
    public ResponseEntity<PaymentMethodDTO> createPaymentMethod(
            @Valid @RequestBody PaymentMethodDTO dto
    ) {
        messagingTemplate.convertAndSend(PAYMENT_METHOD_TOPIC,"PAYMENT_CREATED");
        return ResponseEntity.ok(paymentMethodService.createPaymentMethod(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethod(
            @PathVariable Integer id,
            @Valid @RequestBody PaymentMethodDTO dto
    ) {
        messagingTemplate.convertAndSend(PAYMENT_METHOD_TOPIC,"PAYMENT_UPDATE");
        return ResponseEntity.ok(paymentMethodService.updatePaymentMethod(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Integer id) {
        messagingTemplate.convertAndSend(PAYMENT_METHOD_TOPIC,"PAYMENT_DELETE");
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<PaymentMethodDTO> togglePaymentMethodStatus(
            @PathVariable Integer id
    ) {
        messagingTemplate.convertAndSend(PAYMENT_METHOD_TOPIC,"PAYMENT_STATUS");
        return ResponseEntity.ok(paymentMethodService.togglePaymentMethodStatus(id));
    }
}
