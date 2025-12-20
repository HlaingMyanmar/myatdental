package org.myatdental.treatmentcategoriesoptions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentcategoriesoptions.dto.TreatmentCategoriesDTO;
import org.myatdental.treatmentcategoriesoptions.service.TreatmentCategoriesService;
import org.myatdental.treatmentoptions.dto.TreatmentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatment-categories")
@RequiredArgsConstructor
public class TreatmentCategoriesController {

    private final TreatmentCategoriesService treatmentCategoriesService;


    @GetMapping
    public ResponseEntity<List<TreatmentCategoriesDTO>> getAllCategories() {
        return ResponseEntity.ok(treatmentCategoriesService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentCategoriesDTO> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(treatmentCategoriesService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<TreatmentCategoriesDTO> createCategory(@Valid @RequestBody TreatmentCategoriesDTO dto) {
        return ResponseEntity.ok(treatmentCategoriesService.createCategory(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentCategoriesDTO> updateCategory(
            @PathVariable Integer id,
            @RequestBody TreatmentCategoriesDTO dto
    ) {
        return ResponseEntity.ok(treatmentCategoriesService.updateCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        treatmentCategoriesService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/treatments")
    public ResponseEntity<List<TreatmentDTO>> getTreatmentsByCategory(@PathVariable Integer id) {
        return ResponseEntity.ok(treatmentCategoriesService.getTreatmentsByCategoryId(id));
    }

    @PostMapping("/{categoryId}/treatments")
    public ResponseEntity<TreatmentDTO> assignTreatment(
            @PathVariable Integer categoryId,
            @Valid @RequestBody TreatmentDTO treatmentDto) {

        TreatmentDTO createdTreatment = treatmentCategoriesService.assignTreatmentToCategory(categoryId, treatmentDto);
        return new ResponseEntity<>(createdTreatment, HttpStatus.CREATED);
    }
    @DeleteMapping("/{categoryId}/treatments/{treatmentId}")
    public ResponseEntity<Void> removeTreatment(
            @PathVariable Integer categoryId,
            @PathVariable Integer treatmentId) {

        treatmentCategoriesService.removeTreatmentFromCategory(categoryId, treatmentId);
        return ResponseEntity.noContent().build();
    }
}
