package org.myatdental.dentistoptions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.appointmentoptions.dto.AppointmentDTO;
import org.myatdental.appointmentoptions.service.AppointmentService;
import org.myatdental.treatmentrecord.dto.TreatmentRecordDTO;
import org.myatdental.treatmentrecord.service.TreatmentRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/doctor")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
public class DoctorActivityController {

    private final AppointmentService appointmentService;

    private final SimpMessagingTemplate messagingTemplate;

    private final TreatmentRecordService treatmentRecordService;

    private static final String CHARGE_TOPIC = "/topic/doctor-activity";


    @GetMapping("/my-queue")
    public ResponseEntity<List<AppointmentDTO>> getMyQueue(Authentication auth) {
        return ResponseEntity.ok(appointmentService.getDoctorActiveQueue(auth.getName()));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDTO>> getMyAppointments(Authentication auth) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(auth.getName()));
    }


    @PatchMapping("/appointments/{id}/start")
    public ResponseEntity<AppointmentDTO> startTreatment(
            @PathVariable Integer id,
            Authentication auth) {
        messagingTemplate.convertAndSend(CHARGE_TOPIC, "TREATMENT_STARTED");
        return ResponseEntity.ok(appointmentService.startTreatmentByDoctor(id, auth.getName()));
    }

    @PostMapping("/appointment/{appointmentId}/treatment-records")
    public ResponseEntity<List<TreatmentRecordDTO>> createTreatmentRecordsForAppointment(
            @PathVariable Integer appointmentId,
            @Valid @RequestBody TreatmentRecordService.BulkTreatmentRequestDTO bulkRequestDto) {
        bulkRequestDto.setAppointmentId(appointmentId);
        List<TreatmentRecordDTO> createdRecords = treatmentRecordService.createBulkRecords(bulkRequestDto);
        messagingTemplate.convertAndSend(CHARGE_TOPIC, "TREATMENT_STARTED_RECORDS");
        return new ResponseEntity<>(createdRecords, HttpStatus.CREATED);
    }
}