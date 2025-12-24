package org.myatdental.appointmentoptions.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.appointmentoptions.dto.AppointmentDTO;
import org.myatdental.appointmentoptions.model.Appointment;
import org.myatdental.appointmentoptions.repository.AppointmentRepository;
import org.myatdental.appointmentoptions.status.AppointmentStatus;
import org.myatdental.dentistoptions.model.Dentist;
import org.myatdental.dentistoptions.repository.DentistRepository;
import org.myatdental.patientoption.respository.PatientRepository;
import org.myatdental.roomoptions.model.Room;
import org.myatdental.roomoptions.repository.RoomRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentById(Integer id) {
        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        return convertToDTO(appt);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDateRange(LocalDate start, LocalDate end) {
        return appointmentRepository.findByAppointmentDateBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        // Availability Check
        validateAvailability(dto.getDentistId(), dto.getRoomId(), dto.getAppointmentDate(),
                dto.getAppointmentTime(), dto.getDurationMinutes(), null);

        Appointment appointment = new Appointment();
        if (dto.getCode() == null || dto.getCode().isEmpty()) {
            appointment.setCode("APP-" + (System.currentTimeMillis() % 1000000));
        } else {
            appointment.setCode(dto.getCode());
        }

        mapDtoToEntity(dto, appointment);
        appointment.setStatus(AppointmentStatus.Scheduled);
        assignToken(appointment);

        return convertToDTO(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentDTO updateAppointment(Integer id, AppointmentDTO dto) {

        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));


        if (appt.getStatus() == AppointmentStatus.Completed || appt.getStatus() == AppointmentStatus.Cancelled) {
            throw new RuntimeException("ပြီးဆုံး/ပယ်ဖျက်ထားသော ရက်ချိန်းကို ပြင်၍မရပါ။");
        }


        boolean isDateChanged = !appt.getAppointmentDate().equals(dto.getAppointmentDate());
        boolean isTimeChanged = !appt.getAppointmentTime().equals(dto.getAppointmentTime());


        boolean isDentistChanged = !appt.getDentist().getId().equals(Long.valueOf(dto.getDentistId()));


        Integer currentRoomId = (appt.getRoom() != null) ? appt.getRoom().getRoomId() : null;
        boolean isRoomChanged = (currentRoomId == null && dto.getRoomId() != null) ||
                (currentRoomId != null && !currentRoomId.equals(dto.getRoomId()));


        if (isDateChanged || isTimeChanged || isDentistChanged || isRoomChanged) {
            validateAvailability(
                    dto.getDentistId(),
                    dto.getRoomId(),
                    dto.getAppointmentDate(),
                    dto.getAppointmentTime(),
                    dto.getDurationMinutes(),
                    id
            );


            if (isDateChanged) {
                appt.setAppointmentDate(dto.getAppointmentDate());
                assignToken(appt);
            }
        }


        appt.setAppointmentTime(dto.getAppointmentTime());
        appt.setDurationMinutes(dto.getDurationMinutes());
        appt.setNotes(dto.getNotes());
        appt.setPatientPlanId(dto.getPatientPlanId());


        if (isDentistChanged) {
            Dentist newDentist = dentistRepository.findById(Long.valueOf(dto.getDentistId()))
                    .orElseThrow(() -> new RuntimeException("Dentist not found"));
            appt.setDentist(newDentist);
        }


        if (isRoomChanged) {
            if (dto.getRoomId() != null) {
                Room newRoom = roomRepository.findById(dto.getRoomId())
                        .orElseThrow(() -> new RuntimeException("Room not found"));
                appt.setRoom(newRoom);
            } else {
                appt.setRoom(null);
            }
        }

        return convertToDTO(appointmentRepository.save(appt));
    }

    @Transactional
    public AppointmentDTO updateStatus(Integer id, AppointmentStatus newStatus) {
        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appt.setStatus(newStatus);
        if (newStatus == AppointmentStatus.Checked_In) appt.setCheckInTime(LocalDateTime.now());
        return convertToDTO(appointmentRepository.save(appt));
    }

    // --- Helper Methods ---

    private void validateAvailability(Integer dentistId, Integer roomId, LocalDate date, LocalTime startTime, Integer duration, Integer currentId) {
        LocalTime endTime = startTime.plusMinutes(duration != null ? duration : 30);

        if (appointmentRepository.countOverlappingDentistAppt(dentistId, date, startTime, endTime, currentId) > 0) {
            throw new RuntimeException("ဆရာဝန်တွင် ထိုအချိန်၌ ရက်ချိန်းရှိနေပါသည်။");
        }
        if (roomId != null && appointmentRepository.countOverlappingRoomAppt(roomId, date, startTime, endTime, currentId) > 0) {
            throw new RuntimeException("ရွေးချယ်ထားသော အခန်းမှာ မအားသေးပါ။");
        }
    }

    private void assignToken(Appointment appt) {
        Integer lastToken = appointmentRepository.findMaxTokenByDate(appt.getAppointmentDate());
        appt.setTokenNumber(lastToken == null ? 1 : lastToken + 1);
    }

    private void mapDtoToEntity(AppointmentDTO dto, Appointment appointment) {
        appointment.setAppointmentDate(dto.getAppointmentDate() != null ? dto.getAppointmentDate() : LocalDate.now());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setDurationMinutes(dto.getDurationMinutes() != null ? dto.getDurationMinutes() : 30);
        appointment.setNotes(dto.getNotes());
        appointment.setPatient(patientRepository.findById(dto.getPatientId()).orElseThrow());
        appointment.setDentist(dentistRepository.findById(Long.valueOf(dto.getDentistId())).orElseThrow());
        if (dto.getRoomId() != null) {
            appointment.setRoom(roomRepository.findById(dto.getRoomId()).orElse(null));
        }
    }

    private AppointmentDTO convertToDTO(Appointment appt) {
        AppointmentDTO dto = new AppointmentDTO();
        BeanUtils.copyProperties(appt, dto);

        // [အရေးကြီး] ID ကို လက်နဲ့ Mapping လုပ်ပေးခြင်း
        dto.setAppointmentId(appt.getAppointmentId());

        if (appt.getPatient() != null) dto.setPatientId(appt.getPatient().getId());
        if (appt.getDentist() != null) dto.setDentistId(Math.toIntExact(appt.getDentist().getId()));
        if (appt.getRoom() != null) dto.setRoomId(appt.getRoom().getRoomId());
        if (appt.getPreviousAppointment() != null) dto.setPreviousAppointmentId(appt.getPreviousAppointment().getAppointmentId());
        if (appt.getStatus() != null) dto.setStatus(appt.getStatus().name());

        return dto;
    }

    @Transactional public AppointmentDTO checkIn(Integer id) { return updateStatus(id, AppointmentStatus.Checked_In); }
    @Transactional public AppointmentDTO startTreatment(Integer id) { return updateStatus(id, AppointmentStatus.In_Progress); }
    @Transactional public AppointmentDTO completeAppointment(Integer id) { return updateStatus(id, AppointmentStatus.Completed); }
    @Transactional public AppointmentDTO cancelAppointment(Integer id) { return updateStatus(id, AppointmentStatus.Cancelled); }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getTodayQueue() {
        return appointmentRepository.findByAppointmentDateOrderByTokenNumberAsc(LocalDate.now())
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public void deleteAppointment(Integer id) {
        appointmentRepository.deleteById(id);
    }

    @Transactional
    public AppointmentDTO changeRoom(Integer id, Integer roomId) {
        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        validateAvailability(
                Math.toIntExact(appt.getDentist().getId()),
                roomId,
                appt.getAppointmentDate(),
                appt.getAppointmentTime(),
                appt.getDurationMinutes(),
                id
        );

        Room newRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        appt.setRoom(newRoom);
        return convertToDTO(appointmentRepository.save(appt));
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDoctor(String username) {
        Dentist doctor = dentistRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        return appointmentRepository.findByDentist_Id(doctor.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    // AppointmentService.java ထဲတွင် ထည့်ရန်

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getDoctorActiveQueue(String username) {
        // ၁။ Username ဖြင့် Dentist ကို ရှာသည်
        Dentist doctor = dentistRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // ၂။ 'Checked-In' နှင့် 'In-Progress' ဖြစ်နေသော လူနာများကိုသာ ဆွဲထုတ်သည်
        List<AppointmentStatus> activeStatuses = List.of(AppointmentStatus.Checked_In, AppointmentStatus.In_Progress);

        // Repository တွင် findByDentist_IdAndStatusInOrderByTokenNumberAsc လိုအပ်သည်
        return appointmentRepository.findByDentist_IdAndStatusInOrderByTokenNumberAsc(doctor.getId(), activeStatuses)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    // AppointmentService.java ထဲတွင် ထည့်ရန်

    @Transactional
    public AppointmentDTO startTreatmentByDoctor(Integer appointmentId, String dentistUsername) {
        // ၁။ ရက်ချိန်း (Appointment) ကို ရှာသည်
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // ၂။ [Security Check] ဒီရက်ချိန်းက လက်ရှိ Login ဝင်ထားတဲ့ ဆရာဝန်နဲ့ဆိုင်သလား စစ်သည်
        if (!appt.getDentist().getUser().getUsername().equals(dentistUsername)) {
            throw new RuntimeException("သင်သည် ဤလူနာကို ကုသမှုစတင်ခွင့်မရှိပါ။");
        }

        // ၃။ ပယ်ဖျက်ပြီးသား သို့မဟုတ် ပြီးဆုံးပြီးသားဆိုလျှင် ပြောင်းခွင့်မပေးပါ
        if (appt.getStatus() == AppointmentStatus.Cancelled || appt.getStatus() == AppointmentStatus.Completed) {
            throw new RuntimeException("ဤရက်ချိန်းမှာ ပယ်ဖျက်ထားခြင်း (သို့မဟုတ်) ပြီးဆုံးသွားခြင်း ဖြစ်သောကြောင့် ကုသမှုစတင်၍မရပါ။");
        }

        // ၄။ Status ကို 'In_Progress' သို့ တိုက်ရိုက်ပြောင်းလဲသည်
        appt.setStatus(AppointmentStatus.In_Progress);

        // ၅။ လူနာ ရောက်ရှိချိန် (Check-in Time) မရှိသေးလျှင် ယခုအချိန်ကို ထည့်ပေးမည်
        if (appt.getCheckInTime() == null) {
            appt.setCheckInTime(LocalDateTime.now());
        }

        // ၆။ သိမ်းဆည်းပြီး DTO အဖြစ် ပြန်ထုတ်ပေးသည်
        return convertToDTO(appointmentRepository.save(appt));
    }
}