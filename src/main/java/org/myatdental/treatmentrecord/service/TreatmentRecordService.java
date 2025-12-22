package org.myatdental.treatmentrecord.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.appointmentoptions.model.Appointment;
import org.myatdental.appointmentoptions.repository.AppointmentRepository;
import org.myatdental.appointmentoptions.status.AppointmentStatus;
import org.myatdental.treatmentoptions.model.Treatments;
import org.myatdental.treatmentoptions.repository.TreatmentsRepository;
import org.myatdental.treatmentrecord.dto.TreatmentRecordDTO;
import org.myatdental.treatmentrecord.model.TreatmentRecord;
import org.myatdental.treatmentrecord.repository.TreatmentRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentRecordService {

    private final TreatmentRecordRepository treatmentRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final TreatmentsRepository treatmentsRepository;

    @Transactional(readOnly = true)
    public List<TreatmentRecordDTO> getAllRecords() {
        return treatmentRecordRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TreatmentRecordDTO getRecordById(Long id) {
        TreatmentRecord record = treatmentRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment Record not found with id: " + id));
        return convertToDTO(record);
    }

    @Transactional
    public TreatmentRecordDTO createRecord(TreatmentRecordDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + dto.getAppointmentId()));

        Treatments treatment = treatmentsRepository.findById(dto.getTreatmentId())
                .orElseThrow(() -> new RuntimeException("Treatment type not found with id: " + dto.getTreatmentId()));

        TreatmentRecord record = new TreatmentRecord();
        record.setAppointment(appointment);
        record.setTreatment(treatment);
        record.setToothOrSite(dto.getToothOrSite());
        record.setDiagnosis(dto.getDiagnosis());
        record.setPriceCharged(dto.getPriceCharged());
        record.setRecordNotes(dto.getRecordNotes());
        record.setPerformedAt(dto.getPerformedAt() != null ? dto.getPerformedAt() : LocalDateTime.now());

        // [Logic သစ်] ကုသမှုမှတ်တမ်း သွင်းပြီးလျှင် Appointment ကို Completed ပြောင်းပေးရမည်
        appointment.setStatus(AppointmentStatus.Completed);
        appointmentRepository.save(appointment);

        return convertToDTO(treatmentRecordRepository.save(record));
    }

    @Transactional
    public TreatmentRecordDTO updateRecord(Long id, TreatmentRecordDTO dto) {
        TreatmentRecord record = treatmentRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment Record not found"));

        record.setToothOrSite(dto.getToothOrSite());
        record.setDiagnosis(dto.getDiagnosis());
        record.setPriceCharged(dto.getPriceCharged());
        record.setRecordNotes(dto.getRecordNotes());

        return convertToDTO(treatmentRecordRepository.save(record));
    }

    @Transactional
    public void deleteRecord(Long id) {
        if (!treatmentRecordRepository.existsById(id)) {
            throw new RuntimeException("Record not found");
        }
        treatmentRecordRepository.deleteById(id);
    }

    private TreatmentRecordDTO convertToDTO(TreatmentRecord record) {
        TreatmentRecordDTO dto = new TreatmentRecordDTO();
        dto.setRecordId(record.getRecordId());

        // [Bug Fixed] - record.getRecordId() အစား Appointment ID အစစ်ကို ယူရန်
        if (record.getAppointment() != null) {
            dto.setAppointmentId(record.getAppointment().getAppointmentId());
        }

        if (record.getTreatment() != null) {
            dto.setTreatmentId(record.getTreatment().getTreatmentId());
        }

        dto.setToothOrSite(record.getToothOrSite());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setPriceCharged(record.getPriceCharged());
        dto.setRecordNotes(record.getRecordNotes());
        dto.setPerformedAt(record.getPerformedAt());
        return dto;
    }
}