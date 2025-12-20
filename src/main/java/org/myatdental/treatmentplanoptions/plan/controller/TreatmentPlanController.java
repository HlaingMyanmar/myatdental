package org.myatdental.treatmentplanoptions.plan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentplanoptions.plan.dto.TreatmentPlanDTO;
import org.myatdental.treatmentplanoptions.plan.service.TreatmentPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatment-plans")
@RequiredArgsConstructor
public class TreatmentPlanController {

    private final TreatmentPlanService planService;


    @PostMapping
    public ResponseEntity<TreatmentPlanDTO> createPlan(@Valid @RequestBody TreatmentPlanDTO dto) {
        TreatmentPlanDTO createdPlan = planService.createPlan(dto);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TreatmentPlanDTO> getPlanById(@PathVariable Integer id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }


    @GetMapping("/templates")
    public ResponseEntity<List<TreatmentPlanDTO>> getTemplates() {
        return ResponseEntity.ok(planService.getTemplates());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentPlanDTO> updatePlan(
            @PathVariable Integer id,
            @Valid @RequestBody TreatmentPlanDTO dto) {
        TreatmentPlanDTO updatedPlan = planService.updatePlan(id, dto);
        return ResponseEntity.ok(updatedPlan);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Integer id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}