package org.myatdental.dentistoptions.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.appointmentoptions.dto.AppointmentDTO;
import org.myatdental.appointmentoptions.service.AppointmentService;
import org.springframework.http.ResponseEntity;
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

//     ဆရာဝန် သိရှိရန် - ယခု ဘယ်အခန်းမှာ ဘယ်သူ့ကို ကြည့်ရမလဲဆိုသည့် စာရင်း
    @GetMapping("/my-queue")
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW')")
    public ResponseEntity<List<AppointmentDTO>> getMyQueue(Authentication auth) {
        // ဤ API က 'Checked-In' နှင့် 'In-Progress' လူနာများကိုသာ အခန်းနံပါတ်နှင့်တကွ ပြပေးမည်
        return ResponseEntity.ok(appointmentService.getDoctorActiveQueue(auth.getName()));
    }

    // ဆရာဝန် မိမိ၏ ရက်ချိန်းမှတ်တမ်းအားလုံး (ပြီးဆုံးပြီးသားရော၊ အားလုံးပါဝင်သည်)
    @GetMapping("/appointments")
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW')")
    public ResponseEntity<List<AppointmentDTO>> getMyAppointments(Authentication auth) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(auth.getName()));
    }
    // DoctorActivityController.java ထဲတွင် ထည့်ရန်

    @PatchMapping("/appointments/{id}/start")
    @PreAuthorize("hasRole('ROLE_Doctor')") // ဆရာဝန်များသာ ခေါ်ယူနိုင်သည်
    public ResponseEntity<AppointmentDTO> startTreatment(
            @PathVariable Integer id,
            Authentication auth) {

        // auth.getName() မှတစ်ဆင့် Login ဝင်ထားသော ဆရာဝန်ကို စစ်ဆေးပြီး Status ပြောင်းမည်
        return ResponseEntity.ok(appointmentService.startTreatmentByDoctor(id, auth.getName()));
    }
}