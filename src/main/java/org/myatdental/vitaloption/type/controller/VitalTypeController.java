package org.myatdental.vitaloption.type.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.vitaloption.type.dto.VitalTypeDTO;
import org.myatdental.vitaloption.type.service.VitalTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vital-types")
@RequiredArgsConstructor
public class VitalTypeController {

    private final VitalTypeService vitalTypeService;

    private final SimpMessagingTemplate messagingTemplate;

    private static final String CHARGE_TOPIC = "/topic/vital-types";


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<VitalTypeDTO> createVitalType(@Valid @RequestBody VitalTypeDTO dto) {
        VitalTypeDTO createdVitalType = vitalTypeService.createVitalType(dto);
        messagingTemplate.convertAndSend(CHARGE_TOPIC,"CREATED_Vital_TYPE");
        return new ResponseEntity<>(createdVitalType, HttpStatus.CREATED);
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<List<VitalTypeDTO>> getAllVitalTypes() {
        List<VitalTypeDTO> vitalTypes = vitalTypeService.getAllVitalTypes();
        return ResponseEntity.ok(vitalTypes);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<VitalTypeDTO> getVitalTypeById(@PathVariable Integer id) {
        VitalTypeDTO vitalType = vitalTypeService.getVitalTypeById(id);
        return ResponseEntity.ok(vitalType);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<VitalTypeDTO> updateVitalType(@PathVariable Integer id, @Valid @RequestBody VitalTypeDTO dto) {
        VitalTypeDTO updatedVitalType = vitalTypeService.updateVitalType(id, dto);
        messagingTemplate.convertAndSend(CHARGE_TOPIC,"UPDATED_Vital_TYPE");
        return ResponseEntity.ok(updatedVitalType);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVitalType(@PathVariable Integer id) {
        vitalTypeService.deleteVitalType(id);
        messagingTemplate.convertAndSend(CHARGE_TOPIC,"DELETE_Vital_TYPE");
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<VitalTypeDTO> toggleVitalTypeStatus(@PathVariable Integer id) {
        VitalTypeDTO updatedVitalType = vitalTypeService.toggleVitalTypeStatus(id);
        messagingTemplate.convertAndSend(CHARGE_TOPIC,"STATUS_Vital_TYPE");
        return ResponseEntity.ok(updatedVitalType);
    }
}