package org.myatdental.dentistoptions.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.dentistoptions.dto.DentistDTO;
import org.myatdental.dentistoptions.service.DentistService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DentistDTO> createDentist(@RequestBody DentistDTO dto) {
        return ResponseEntity.ok(dentistService.createDentist(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<DentistDTO> updateDentist(
            @PathVariable Long id,
            @RequestBody DentistDTO dto
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