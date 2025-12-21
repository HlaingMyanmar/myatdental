package org.myatdental.appointmentoptions.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.appointmentoptions.dto.AppointmentDTO;
import org.myatdental.appointmentoptions.service.AppointmentService;
import org.myatdental.appointmentoptions.status.AppointmentStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // ၁။ ရက်ချိန်းအားလုံးကို ပြန်ထုတ်ပေးခြင်း
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // ၂။ ရက်စွဲအပိုင်းအခြားအလိုက် ရှာဖွေခြင်း (ဥပမာ- /api/appointments/range?start=2023-10-01&end=2023-10-31)
    @GetMapping("/range")
    public ResponseEntity<List<AppointmentDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRange(start, end));
    }

    // ၃။ ID ဖြင့် တစ်ခုချင်းရှာဖွေခြင်း
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    // ၄။ ရက်ချိန်းအသစ်ဆောက်ခြင်း
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO dto) {
        return new ResponseEntity<>(appointmentService.createAppointment(dto), HttpStatus.CREATED);
    }

    // ၅။ ရက်ချိန်း အချက်အလက်ပြင်ဆင်ခြင်း
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Integer id, @RequestBody AppointmentDTO dto) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, dto));
    }

    // --- Status Update Endpoints (ဆေးခန်းလုပ်ငန်းစဉ်များ) ---

    // ၆။ လူနာရောက်ရှိကြောင်း မှတ်သားခြင်း (Check-In)
    @PatchMapping("/{id}/check-in")
    public ResponseEntity<AppointmentDTO> checkIn(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.checkIn(id));
    }

    // ၇။ ကုသမှုစတင်ခြင်း (Start Treatment)
    @PatchMapping("/{id}/start")
    public ResponseEntity<AppointmentDTO> startTreatment(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.startTreatment(id));
    }

    // ၈။ ကုသမှုပြီးဆုံးခြင်း (Complete)
    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentDTO> completeAppointment(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.completeAppointment(id));
    }

    // ၉။ ရက်ချိန်းပယ်ဖျက်ခြင်း (Cancel)
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    // ၁၀။ Status တစ်ခုခုသို့ တိုက်ရိုက်ပြောင်းလဲခြင်း
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDTO> updateStatus(@PathVariable Integer id, @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    // ၁၁။ ယနေ့ တိုကင်အစီအစဉ်အတိုင်း စောင့်ဆိုင်းစာရင်းကြည့်ရန်
    @GetMapping("/today-queue")
    public ResponseEntity<List<AppointmentDTO>> getTodayQueue() {
        return ResponseEntity.ok(appointmentService.getTodayQueue());
    }

    // ၁၂။ ရက်ချိန်းကို Database ထဲမှ လုံးဝဖျက်ထုတ်ခြင်း
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}