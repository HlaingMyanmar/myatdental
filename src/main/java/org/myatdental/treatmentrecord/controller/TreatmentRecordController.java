package org.myatdental.treatmentrecord.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentrecord.dto.TreatmentRecordDTO;
import org.myatdental.treatmentrecord.service.TreatmentRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/treatment-records")
@RequiredArgsConstructor
public class TreatmentRecordController {

    private final TreatmentRecordService treatmentRecordService;

    private final SimpMessagingTemplate messagingTemplate;

    private static final String TREATMENT_RECORD_TOPIC = "/topic/TREATMENT_RECORD";


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR')")
    public ResponseEntity<List<TreatmentRecordDTO>> getAllRecords() {
        return ResponseEntity.ok(treatmentRecordService.getAllRecords());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR')")
    public ResponseEntity<TreatmentRecordDTO> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(treatmentRecordService.getRecordById(id));
    }

//    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR')")
//    public ResponseEntity<TreatmentRecordDTO> createRecord(@Valid @RequestBody TreatmentRecordDTO dto) {
//     messagingTemplate.convertAndSend(TREATMENT_RECORD_TOPIC,"TREATMENT_RECORD_INSERT");
//        return ResponseEntity.ok(treatmentRecordService.createRecord(dto));
//    }

    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR')")
    public ResponseEntity<List<TreatmentRecordDTO>> createBulkRecords(@Valid @RequestBody TreatmentRecordService.BulkTreatmentRequestDTO dto) {
       messagingTemplate.convertAndSend(TREATMENT_RECORD_TOPIC,"TREATMENT_RECORD_INSERT_BULK");
        return ResponseEntity.ok(treatmentRecordService.createBulkRecords(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR')")
    public ResponseEntity<TreatmentRecordDTO> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody TreatmentRecordDTO dto) {
        messagingTemplate.convertAndSend(TREATMENT_RECORD_TOPIC,"TREATMENT_RECORD_UPDATE");
        return ResponseEntity.ok(treatmentRecordService.updateRecord(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        messagingTemplate.convertAndSend(TREATMENT_RECORD_TOPIC,"TREATMENT_RECORD_DELETE");
        treatmentRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}