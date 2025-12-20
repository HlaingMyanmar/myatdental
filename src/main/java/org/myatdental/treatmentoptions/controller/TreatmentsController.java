package org.myatdental.treatmentoptions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentoptions.dto.TreatmentDTO;
import org.myatdental.treatmentoptions.service.TreatmentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
public class TreatmentsController {

    private final TreatmentService treatmentService;

    @GetMapping
    public ResponseEntity<List<TreatmentDTO>> getAllTreatments() {
        return ResponseEntity.ok(treatmentService.getAllTreatments());
    }


    @PostMapping
    public ResponseEntity<TreatmentDTO> createTreatment(@Valid @RequestBody TreatmentDTO dto) {
        return new ResponseEntity<>(treatmentService.createTreatment(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentDTO> getTreatmentById(@PathVariable Integer id) {
        return ResponseEntity.ok(treatmentService.getTreatmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentDTO> updateTreatment(@PathVariable Integer id, @Valid @RequestBody TreatmentDTO dto) {
        return ResponseEntity.ok(treatmentService.updateTreatment(id, dto));
    }
}