package org.myatdental.vitaloption.patient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.vitaloption.patient.model.PatientVital;
import org.myatdental.vitaloption.patient.service.PatientVitalService;
import org.myatdental.vitaloption.patient.dto.PatientVitalDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient-vitals")
@RequiredArgsConstructor
public class PatientVitalController {

    private final PatientVitalService patientVitalService;

    private final SimpMessagingTemplate messagingTemplate;

    private static final String CHARGE_TOPIC = "/topic/patient-vitals";

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<PatientVitalDTO> createPatientVital(@Valid @RequestBody PatientVitalDTO dto) {
        PatientVitalDTO createdVital = patientVitalService.createPatientVital(dto);
        messagingTemplate.convertAndSend(CHARGE_TOPIC,"CREATED_PATIENT_VITAL");
        return new ResponseEntity<>(createdVital, HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<List<PatientVitalDTO>> getAllPatientVitals() {
        List<PatientVitalDTO> vitals = patientVitalService.getAllPatientVitals();
        return ResponseEntity.ok(vitals);
    }


    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<List<PatientVitalDTO>> getPatientVitalsByPatientId(@PathVariable Integer patientId) {
        List<PatientVitalDTO> vitals = patientVitalService.getPatientVitalsByPatientId(patientId);
        return ResponseEntity.ok(vitals);
    }
    @PostMapping("/bulk")
    public ResponseEntity<List<PatientVital>> createMultiplePatientVitals(@RequestBody List<PatientVital> patientVitals) {
        if (patientVitals == null || patientVitals.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            List<PatientVital> createdVitals = patientVitalService.createMultiplePatientVitals(patientVitals);
            return new ResponseEntity<>(createdVitals, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<List<PatientVitalDTO>> getPatientVitalsByAppointmentId(@PathVariable Integer appointmentId) {
        List<PatientVitalDTO> vitals = patientVitalService.getPatientVitalsByAppointmentId(appointmentId);
        return ResponseEntity.ok(vitals);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<PatientVitalDTO> getPatientVitalById(@PathVariable Integer id) {
        PatientVitalDTO vital = patientVitalService.getPatientVitalById(id);
        return ResponseEntity.ok(vital);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<PatientVitalDTO> updatePatientVital(@PathVariable Integer id, @Valid @RequestBody PatientVitalDTO dto) {
        PatientVitalDTO updatedVital = patientVitalService.updatePatientVital(id, dto);
        messagingTemplate.convertAndSend(CHARGE_TOPIC,"UPDATE_PATIENT_VITAL");
        return ResponseEntity.ok(updatedVital);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatientVital(@PathVariable Integer id) {
        patientVitalService.deletePatientVital(id);
        messagingTemplate.convertAndSend(CHARGE_TOPIC,"DELTE_PATIENT_VITAL");
        return ResponseEntity.noContent().build();
    }
}