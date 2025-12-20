package org.myatdental.treatmentoptions.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentoptions.dto.TreatmentDTO;
import org.myatdental.treatmentoptions.service.TreatmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
public class TreatmentsController {

    private final TreatmentService treatmentsService;

    @GetMapping
    public ResponseEntity<List<TreatmentDTO>> getAllTreatments() {
        return ResponseEntity.ok(treatmentsService.getAllTreatments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentDTO> getTreatmentById(@PathVariable Integer id) {
        return ResponseEntity.ok(treatmentsService.getTreatmentById(id));
    }

    @PostMapping
    public ResponseEntity<TreatmentDTO> createTreatment(@Valid @RequestBody TreatmentDTO dto) {
        return ResponseEntity.ok(treatmentsService.createTreatment(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentDTO> updateTreatment(
            @PathVariable Integer id,
            @Valid @RequestBody TreatmentDTO dto
    ) {
        return ResponseEntity.ok(treatmentsService.updateTreatment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Integer id) {
        treatmentsService.deleteTreatment(id);
        return ResponseEntity.noContent().build();
    }
}

