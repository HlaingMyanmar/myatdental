package org.myatdental.patientoption.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.patientoption.dto.PatientDTO;
import org.myatdental.patientoption.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    private final SimpMessagingTemplate messagingTemplate;

    private static final String PATIENT_TOPIC = "/topic/patients";

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Integer id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(
            @Valid @RequestBody PatientDTO dto
    ) {
        messagingTemplate.convertAndSend(PATIENT_TOPIC,"PATIENT_CREATED");
        return ResponseEntity.ok(patientService.createPatient(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable Integer id,
            @RequestBody PatientDTO dto
    ) {
        messagingTemplate.convertAndSend(PATIENT_TOPIC,"PATIENT_UPDATED");
        return ResponseEntity.ok(patientService.updatePatient(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        messagingTemplate.convertAndSend(PATIENT_TOPIC,"PATIENT_DELETED");
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}

