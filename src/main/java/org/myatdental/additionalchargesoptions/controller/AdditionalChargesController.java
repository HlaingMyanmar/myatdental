package org.myatdental.additionalchargesoptions.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.additionalchargesoptions.dto.AdditionalChargesDTO;
import org.myatdental.additionalchargesoptions.service.AdditionalChargesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charges")
@RequiredArgsConstructor
public class AdditionalChargesController {

    private final AdditionalChargesService additionalChargesService;

    @GetMapping
    public ResponseEntity<List<AdditionalChargesDTO>> getAllCharges() {
        return ResponseEntity.ok(additionalChargesService.getAllCharges());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdditionalChargesDTO> getChargeById(@PathVariable Integer id) {
        return ResponseEntity.ok(additionalChargesService.getChargeById(id));
    }

    @PostMapping
    public ResponseEntity<AdditionalChargesDTO> createCharge(@RequestBody AdditionalChargesDTO dto) {
        return ResponseEntity.ok(additionalChargesService.createCharge(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdditionalChargesDTO> updateCharge(
            @PathVariable Integer id,
            @RequestBody AdditionalChargesDTO dto
    ) {
        return ResponseEntity.ok(additionalChargesService.updateCharge(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharge(@PathVariable Integer id) {
        additionalChargesService.deleteCharge(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<AdditionalChargesDTO> toggleChargeStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(additionalChargesService.toggleChargeStatus(id));
    }
}
