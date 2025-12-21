package org.myatdental.patientplanoption.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.patientplanoption.dto.PatientPlanDTO;
import org.myatdental.patientplanoption.service.PatientPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient-assign-plans")
@RequiredArgsConstructor
public class PatientPlanController {

    private final PatientPlanService patientPlanService;

    // GET: All Patient Plans
    @GetMapping
    public ResponseEntity<List<PatientPlanDTO>> getAllPatientPlans() {
        List<PatientPlanDTO> plans = patientPlanService.getAllPatientPlans();
        return ResponseEntity.ok(plans);
    }

    // GET: Single Patient Plan by ID
    @GetMapping("/{id}")
    public ResponseEntity<PatientPlanDTO> getPatientPlanById(@PathVariable Long id) {
        PatientPlanDTO plan = patientPlanService.getPatientPlanById(id);
        return ResponseEntity.ok(plan);
    }

    // POST: Create new Patient Plan
    @PostMapping
    public ResponseEntity<PatientPlanDTO> createPatientPlan(@Valid @RequestBody PatientPlanDTO dto) {
        PatientPlanDTO createdPlan = patientPlanService.createPatientPlan(dto);
        return ResponseEntity.ok(createdPlan);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PatientPlanDTO> updatePatientPlan(
            @PathVariable Long id,
            @RequestBody PatientPlanDTO dto
    ) {
        PatientPlanDTO updatedPlan = patientPlanService.updatePatientPlan(id, dto);
        return ResponseEntity.ok(updatedPlan);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientPlan(@PathVariable Long id) {
        patientPlanService.deletePatientPlan(id);
        return ResponseEntity.noContent().build();
    }

}
