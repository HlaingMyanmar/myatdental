package org.myatdental.treatmentrecord.service;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        TreatmentRecord record = treatmentRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment Record not found with id: " + id));
        Appointment appointment = record.getAppointment();
        treatmentRecordRepository.delete(record);
        List<TreatmentRecord> remainingRecords = treatmentRecordRepository.findByAppointment_AppointmentId(Long.valueOf(appointment.getAppointmentId()));


        if (remainingRecords.isEmpty()) {
            appointment.setStatus(AppointmentStatus.Scheduled);
            appointmentRepository.save(appointment);
        }
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

    @Data
    public static class BulkTreatmentRequestDTO {
        @NotNull(message = "Appointment ID is required")
        private Integer appointmentId;

        @NotEmpty(message = "At least one treatment is required")
        private List<TreatmentItemDTO> treatments; // ကုသမှုများစာရင်း
    }

    @Data
    public static class TreatmentItemDTO {
        @NotNull(message = "Treatment ID is required")
        private Integer treatmentId;

        private String toothOrSite;
        private String diagnosis;

        @NotNull(message = "Price is required")
        private BigDecimal priceCharged;

        private String recordNotes;
    }
    @Transactional
    public List<TreatmentRecordDTO> createBulkRecords(BulkTreatmentRequestDTO bulkDto) {

        Appointment appointment = appointmentRepository.findById(bulkDto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        List<TreatmentRecord> recordsToSave = new ArrayList<>();

        for (TreatmentItemDTO item : bulkDto.getTreatments()) {
            Treatments treatment = treatmentsRepository.findById(item.getTreatmentId())
                    .orElseThrow(() -> new RuntimeException("Treatment type not found ID: " + item.getTreatmentId()));

            TreatmentRecord record = new TreatmentRecord();
            record.setAppointment(appointment);
            record.setTreatment(treatment);
            record.setToothOrSite(item.getToothOrSite());
            record.setDiagnosis(item.getDiagnosis());
            record.setPriceCharged(item.getPriceCharged());
            record.setRecordNotes(item.getRecordNotes());
            record.setPerformedAt(LocalDateTime.now());
            recordsToSave.add(record);
        }
        List<TreatmentRecord> savedRecords = treatmentRecordRepository.saveAll(recordsToSave);
        appointment.setStatus(AppointmentStatus.Completed);
        appointmentRepository.save(appointment);
        return savedRecords.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}