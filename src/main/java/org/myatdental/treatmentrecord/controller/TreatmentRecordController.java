package org.myatdental.treatmentrecord.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentrecord.dto.TreatmentRecordDTO;
import org.myatdental.treatmentrecord.service.TreatmentRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/treatment-records")
@RequiredArgsConstructor
public class TreatmentRecordController {

    private final TreatmentRecordService treatmentRecordService;

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
//        return ResponseEntity.ok(treatmentRecordService.createRecord(dto));
//    }

    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR')")
    public ResponseEntity<List<TreatmentRecordDTO>> createBulkRecords(@Valid @RequestBody TreatmentRecordService.BulkTreatmentRequestDTO dto) {
        return ResponseEntity.ok(treatmentRecordService.createBulkRecords(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR')")
    public ResponseEntity<TreatmentRecordDTO> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody TreatmentRecordDTO dto) {
        return ResponseEntity.ok(treatmentRecordService.updateRecord(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')") // ဖျက်ခြင်းကို Admin သာ လုပ်နိုင်ရန်
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        treatmentRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}