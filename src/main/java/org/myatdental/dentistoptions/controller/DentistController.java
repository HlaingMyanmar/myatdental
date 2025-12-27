package org.myatdental.dentistoptions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.dentistoptions.dto.DentistDTO;
import org.myatdental.dentistoptions.service.DentistService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // Security အတွက်
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dentists")
@RequiredArgsConstructor
public class DentistController {

    private final DentistService dentistService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String DENTIST_TOPIC = "/topic/dentists";

    @GetMapping
    @PreAuthorize("hasAuthority('CAN_DENTIST_READ')")
    public ResponseEntity<List<DentistDTO>> getAllDentists() {
        return ResponseEntity.ok(dentistService.getAllDentists());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_DENTIST_READ_ID')")
    public ResponseEntity<DentistDTO> getDentistById(@PathVariable Long id) {
        return ResponseEntity.ok(dentistService.getDentistById(id));
    }
    @PostMapping
    @PreAuthorize("hasAuthority('CAN_DENTIST_CREATE')")
    public ResponseEntity<DentistDTO> createDentist(@Valid @RequestBody DentistDTO dto) {
        DentistDTO created = dentistService.createDentist(dto);
        messagingTemplate.convertAndSend(DENTIST_TOPIC, "DENTIST_CREATED");
        return ResponseEntity.ok(created);
    }
    @PreAuthorize("hasAuthority('CAN_DENTIST_UNLINK')")
    @PatchMapping("/{id}/unlink-account")
    public ResponseEntity<DentistDTO> unlinkAccount(@PathVariable Long id) {
        DentistDTO updated = dentistService.unlinkUserAccount(id);
        messagingTemplate.convertAndSend(DENTIST_TOPIC, "DENTIST_ACCOUNT_UNLINKED");
        return ResponseEntity.ok(updated);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_DENTIST_EDIT')")
    public ResponseEntity<DentistDTO> updateDentist(
            @PathVariable Long id,
            @Valid @RequestBody DentistDTO dto
    ) {
        DentistDTO updated = dentistService.updateDentist(id, dto);
        messagingTemplate.convertAndSend(DENTIST_TOPIC, "DENTIST_UPDATED");
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_DENTIST_DELETE')")
    public ResponseEntity<Void> deleteDentist(@PathVariable Long id) {
        dentistService.deleteDentist(id);
        messagingTemplate.convertAndSend(DENTIST_TOPIC, "DENTIST_DELETED");
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasAuthority('CAN_DENTIST_STATUS_CHANGE')")
    public ResponseEntity<DentistDTO> toggleDentistStatus(@PathVariable Long id) {
        DentistDTO updated =dentistService.toggleDentistStatus(id);
        messagingTemplate.convertAndSend(DENTIST_TOPIC, "DENTIST_STATUS_TOGGLED");
        return ResponseEntity.ok(updated);
    }
}