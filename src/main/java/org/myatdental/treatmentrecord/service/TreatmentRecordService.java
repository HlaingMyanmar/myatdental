package org.myatdental.treatmentrecord.service;
import lombok.RequiredArgsConstructor;
import org.myatdental.appointmentoptions.model.Appointment;
import org.myatdental.appointmentoptions.repository.AppointmentRepository;
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
    private final TreatmentsRepository treatmentRepository;


    @Transactional(readOnly = true)
    public List<TreatmentRecordDTO> getAllRecords() {
        return treatmentRecordRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public TreatmentRecordDTO getRecordById(Long id) {
        TreatmentRecord record = treatmentRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TreatmentRecord not found with id: " + id));
        return convertToDTO(record);
    }


    @Transactional
    public TreatmentRecordDTO createRecord(TreatmentRecordDTO dto) {

        Appointment appointment = appointmentRepository.findById(Math.toIntExact(dto.getAppointmentId()))
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found with id: " + dto.getAppointmentId()));

        Treatments treatment = treatmentRepository.findById(Math.toIntExact(dto.getTreatmentId()))
                .orElseThrow(() ->
                        new RuntimeException("Treatment not found with id: " + dto.getTreatmentId()));

        TreatmentRecord record = new TreatmentRecord();
        record.setAppointment(appointment);
        record.setTreatment(treatment);
        record.setToothOrSite(dto.getToothOrSite());
        record.setDiagnosis(dto.getDiagnosis());
        record.setPriceCharged(dto.getPriceCharged());
        record.setRecordNotes(dto.getRecordNotes());
        record.setPerformedAt(dto.getPerformedAt() != null
                ? dto.getPerformedAt()
                : LocalDateTime.now());

        return convertToDTO(treatmentRecordRepository.save(record));
    }


    @Transactional
    public TreatmentRecordDTO updateRecord(Long id, TreatmentRecordDTO dto) {

        TreatmentRecord record = treatmentRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TreatmentRecord not found with id: " + id));

        if (dto.getToothOrSite() != null)
            record.setToothOrSite(dto.getToothOrSite());

        if (dto.getDiagnosis() != null)
            record.setDiagnosis(dto.getDiagnosis());

        if (dto.getPriceCharged() != null)
            record.setPriceCharged(dto.getPriceCharged());

        if (dto.getRecordNotes() != null)
            record.setRecordNotes(dto.getRecordNotes());

        return convertToDTO(treatmentRecordRepository.save(record));
    }

    @Transactional
    public void deleteRecord(Long id) {
        if (!treatmentRecordRepository.existsById(id)) {
            throw new RuntimeException("TreatmentRecord not found with id: " + id);
        }
        treatmentRecordRepository.deleteById(id);
    }


    private TreatmentRecordDTO convertToDTO(TreatmentRecord record) {

        TreatmentRecordDTO dto = new TreatmentRecordDTO();

        dto.setRecordId(record.getRecordId());

        dto.setAppointmentId(
                record.getRecordId());

        dto.setTreatmentId(
                record.getTreatment().getTreatmentId().longValue()
        );

        dto.setToothOrSite(record.getToothOrSite());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setPriceCharged(record.getPriceCharged());
        dto.setRecordNotes(record.getRecordNotes());
        dto.setPerformedAt(record.getPerformedAt());

        return dto;
    }

}

