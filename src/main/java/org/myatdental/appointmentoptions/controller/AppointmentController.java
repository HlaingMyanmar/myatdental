package org.myatdental.appointmentoptions.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.appointmentoptions.dto.AppointmentDTO;
import org.myatdental.appointmentoptions.service.AppointmentService;
import org.myatdental.appointmentoptions.status.AppointmentStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final SimpMessagingTemplate messagingTemplate;
    private static final String APPOINTMENT_TOPIC = "/topic/appointments";

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentDTO>> getAll() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }
    @GetMapping("/range")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentDTO>> getByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRange(start, end));
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> create(@RequestBody AppointmentDTO dto) {
        AppointmentDTO created =  appointmentService.createAppointment(dto);
        messagingTemplate.convertAndSend(APPOINTMENT_TOPIC,"APPOINTMENT_CREATED");
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> update(@PathVariable Integer id, @RequestBody AppointmentDTO dto) {
        AppointmentDTO updated = appointmentService.updateAppointment(id,dto);
        messagingTemplate.convertAndSend(APPOINTMENT_TOPIC,"APPOINTMENT_UPDATED");
        return ResponseEntity.ok(updated);
    }
    @PatchMapping("/{id}/check-in")
    public ResponseEntity<AppointmentDTO> checkIn(@PathVariable Integer id) {
        AppointmentDTO updated = appointmentService.checkIn(id);
        messagingTemplate.convertAndSend(APPOINTMENT_TOPIC,"APPOINTMENT_CHECKED_IN");
        return ResponseEntity.ok(updated);
    }
    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentDTO> complete(@PathVariable Integer id) {
        AppointmentDTO updated = appointmentService.completeAppointment(id);
        messagingTemplate.convertAndSend(APPOINTMENT_TOPIC,"APPOINTMENT_COMPLETE");
        return ResponseEntity.ok(updated);
    }
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancel(@PathVariable Integer id) {
        AppointmentDTO updated = appointmentService.cancelAppointment(id);
        messagingTemplate.convertAndSend(APPOINTMENT_TOPIC,"APPOINTMENT_CANCEL");
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/today-queue")
    public ResponseEntity<List<AppointmentDTO>> getTodayQueue() {
        return ResponseEntity.ok(appointmentService.getTodayQueue());
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        appointmentService.deleteAppointment(id);
        messagingTemplate.convertAndSend(APPOINTMENT_TOPIC,"APPOINTMENT_DELETED");
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/change-room/{roomId}")
    @PreAuthorize("hasAnyAuthority('ROLE_Admin', 'ROLE_Administrator', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> changeRoom(
            @PathVariable Integer id,
            @PathVariable Integer roomId) {
        AppointmentDTO updated = appointmentService.changeRoom(id, roomId);
        messagingTemplate.convertAndSend(APPOINTMENT_TOPIC, "ROOM_CHANGED");
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentDTO>> getCompletedAppointments() {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(AppointmentStatus.Completed));
    }
}