package org.myatdental.dentistoptions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.dentistoptions.dto.DentistDTO;
import org.myatdental.dentistoptions.service.DentistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // Security အတွက်
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dentists")
@RequiredArgsConstructor
public class DentistController {

    private final DentistService dentistService;

    @GetMapping
    public ResponseEntity<List<DentistDTO>> getAllDentists() {
        return ResponseEntity.ok(dentistService.getAllDentists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistDTO> getDentistById(@PathVariable Long id) {
        return ResponseEntity.ok(dentistService.getDentistById(id));
    }

    @PostMapping
    public ResponseEntity<DentistDTO> createDentist(@Valid @RequestBody DentistDTO dto) {
        return ResponseEntity.ok(dentistService.createDentist(dto));
    }
    @PatchMapping("/{id}/unlink-account")
    public ResponseEntity<DentistDTO> unlinkAccount(@PathVariable Long id) {
        return ResponseEntity.ok(dentistService.unlinkUserAccount(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<DentistDTO> updateDentist(
            @PathVariable Long id,
            @Valid @RequestBody DentistDTO dto
    ) {
        return ResponseEntity.ok(dentistService.updateDentist(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDentist(@PathVariable Long id) {
        dentistService.deleteDentist(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<DentistDTO> toggleDentistStatus(@PathVariable Long id) {
        return ResponseEntity.ok(dentistService.toggleDentistStatus(id));
    }
}