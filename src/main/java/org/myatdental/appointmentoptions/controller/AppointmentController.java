package org.myatdental.appointmentoptions.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.appointmentoptions.dto.AppointmentDTO;
import org.myatdental.appointmentoptions.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAll() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/range")
    public ResponseEntity<List<AppointmentDTO>> getByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRange(start, end));
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> create(@RequestBody AppointmentDTO dto) {
        return new ResponseEntity<>(appointmentService.createAppointment(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> update(@PathVariable Integer id, @RequestBody AppointmentDTO dto) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, dto));
    }

    @PatchMapping("/{id}/check-in")
    public ResponseEntity<AppointmentDTO> checkIn(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.checkIn(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentDTO> complete(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.completeAppointment(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancel(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    @GetMapping("/today-queue")
    public ResponseEntity<List<AppointmentDTO>> getTodayQueue() {
        return ResponseEntity.ok(appointmentService.getTodayQueue());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
    // AppointmentController.java ထဲတွင် ထည့်ရန်

    @PatchMapping("/{id}/change-room/{roomId}")
    @PreAuthorize("hasAnyAuthority('ROLE_Admin', 'ROLE_Administrator', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> changeRoom(
            @PathVariable Integer id,
            @PathVariable Integer roomId) {

        System.out.println(id);
        System.out.println(roomId);
        return ResponseEntity.ok(appointmentService.changeRoom(id, roomId));
    }
}