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

    @GetMapping
    public ResponseEntity<List<TreatmentPlanDTO>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @PostMapping("/{templateId}/clone-to-patient")
    public ResponseEntity<TreatmentPlanDTO> cloneTemplateToPatient(@PathVariable Integer templateId) {
        TreatmentPlanDTO clonedPlan = planService.cloneTemplateToPatient(templateId);
        return new ResponseEntity<>(clonedPlan, HttpStatus.CREATED);
    }

    @GetMapping("/templates")
    public ResponseEntity<List<TreatmentPlanDTO>> getTemplates() {
        return ResponseEntity.ok(planService.getTemplates());
    }

    @GetMapping("/patient-plans")
    public ResponseEntity<List<TreatmentPlanDTO>> getPatientPlans() {
        // Template မဟုတ်တဲ့ list တွေကိုပဲ ပြန်ပေးပါမယ်
        List<TreatmentPlanDTO> patientPlans = planService.getPatientPlans();
        return ResponseEntity.ok(patientPlans);
    }

    @PostMapping
    public ResponseEntity<TreatmentPlanDTO> createPlan(@Valid @RequestBody TreatmentPlanDTO dto) {
        TreatmentPlanDTO createdPlan = planService.createPlan(dto);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TreatmentPlanDTO> getPlanById(@PathVariable Integer id) {
        return ResponseEntity.ok(planService.getPlanById(id));
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