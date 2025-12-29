package org.myatdental.additionalchargesoptions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.additionalchargesoptions.dto.AdditionalChargesDTO;
import org.myatdental.additionalchargesoptions.service.AdditionalChargesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate; // WebSocket အတွက်
import org.springframework.security.access.prepost.PreAuthorize; // Security အတွက်
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/additional-charges")
@RequiredArgsConstructor
public class AdditionalChargesController {

    private final AdditionalChargesService additionalChargesService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String CHARGE_TOPIC = "/topic/additional-charges";

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AdMINISTRATOR', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<AdditionalChargesDTO>> getAllCharges() {
        return ResponseEntity.ok(additionalChargesService.getAllCharges());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AdMINISTRATOR', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<AdditionalChargesDTO> getChargeById(@PathVariable Integer id) {
        return ResponseEntity.ok(additionalChargesService.getChargeById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AdMINISTRATOR')")
    public ResponseEntity<AdditionalChargesDTO> createCharge(@Valid @RequestBody AdditionalChargesDTO dto) {
        AdditionalChargesDTO createdCharge = additionalChargesService.createCharge(dto);
        messagingTemplate.convertAndSend(CHARGE_TOPIC, "CHARGE_CREATED");
        return new ResponseEntity<>(createdCharge, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AdMINISTRATOR')")
    public ResponseEntity<AdditionalChargesDTO> updateCharge(
            @PathVariable Integer id,
            @Valid @RequestBody AdditionalChargesDTO dto
    ) {
        AdditionalChargesDTO updated = additionalChargesService.updateCharge(id, dto);
        messagingTemplate.convertAndSend(CHARGE_TOPIC, "CHARGE_UPDATED");
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AdMINISTRATOR')")
    public ResponseEntity<Void> deleteCharge(@PathVariable Integer id) {
        additionalChargesService.deleteCharge(id);

        messagingTemplate.convertAndSend(CHARGE_TOPIC, "CHARGE_DELETED");
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMIN', 'AdMINISTRATOR')")
    public ResponseEntity<AdditionalChargesDTO> toggleChargeStatus(@PathVariable Integer id) {
        AdditionalChargesDTO updated = additionalChargesService.toggleChargeStatus(id);
        messagingTemplate.convertAndSend(CHARGE_TOPIC, "CHARGE_STATUS_TOGGLED");
        return ResponseEntity.ok(updated);
    }
}