package org.myatdental.vitaloption.patient.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.appointmentoptions.model.Appointment;
import org.myatdental.appointmentoptions.repository.AppointmentRepository;

import org.myatdental.authoption.useroptions.model.User;
import org.myatdental.authoption.useroptions.repository.UserRepository;
import org.myatdental.patientoption.model.Patient;
import org.myatdental.patientoption.respository.PatientRepository;
import org.myatdental.vitaloption.patient.dto.PatientVitalDTO;
import org.myatdental.vitaloption.patient.model.PatientVital;
import org.myatdental.vitaloption.patient.repostory.PatientVitalRepository;
import org.myatdental.vitaloption.type.model.VitalType;
import org.myatdental.vitaloption.type.repository.VitalTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientVitalService {

    private final PatientVitalRepository patientVitalRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final VitalTypeRepository vitalTypeRepository;
    private final UserRepository userRepository; // recordedBy အတွက်

    @Transactional
    public PatientVitalDTO createPatientVital(PatientVitalDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + dto.getPatientId()));
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + dto.getAppointmentId()));
        VitalType vitalType = vitalTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("Vital Type not found with ID: " + dto.getTypeId()));

        User recordedBy = null;
        if (dto.getRecordedById() != null) {
            recordedBy = userRepository.findById(dto.getRecordedById())
                    .orElseThrow(() -> new RuntimeException("Recorded By user not found with ID: " + dto.getRecordedById()));
        }

        PatientVital patientVital = new PatientVital();
        patientVital.setPatient(patient);
        patientVital.setAppointment(appointment);
        patientVital.setVitalType(vitalType);
        patientVital.setVitalValue(dto.getVitalValue());
        patientVital.setRecordedBy(recordedBy);
        patientVital.setRecordedAt(LocalDateTime.now()); // အသစ်ဖန်တီးရင် လက်ရှိအချိန်

        return convertToDTO(patientVitalRepository.save(patientVital));
    }

    @Transactional(readOnly = true)
    public List<PatientVitalDTO> getAllPatientVitals() {
        return patientVitalRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PatientVitalDTO> getPatientVitalsByPatientId(Integer patientId) {
        return patientVitalRepository.findByPatient_Id(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PatientVitalDTO> getPatientVitalsByAppointmentId(Integer appointmentId) {
        return patientVitalRepository.findByPatient_Id(appointmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientVitalDTO getPatientVitalById(Integer id) {
        return patientVitalRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Patient Vital not found with ID: " + id));
    }

    @Transactional
    public PatientVitalDTO updatePatientVital(Integer id, PatientVitalDTO dto) {
        PatientVital patientVital = patientVitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient Vital not found with ID: " + id));
        if (dto.getVitalValue() != null) {
            patientVital.setVitalValue(dto.getVitalValue());
        }
        return convertToDTO(patientVitalRepository.save(patientVital));
    }

    @Transactional
    public void deletePatientVital(Integer id) {
        if (!patientVitalRepository.existsById(id)) {
            throw new RuntimeException("Patient Vital not found with ID: " + id);
        }
        patientVitalRepository.deleteById(id);
    }
    @Transactional
    public List<PatientVital> createMultiplePatientVitals(List<PatientVital> patientVitals) {

        for (PatientVital vital : patientVitals) {
            validatePatientVitalForeignKeys(vital);
            if (vital.getRecordedAt() == null) {
                vital.setRecordedAt(LocalDateTime.now());
            }
        }
        return patientVitalRepository.saveAll(patientVitals);
    }
    private void validatePatientVitalForeignKeys(PatientVital vital) {
        if (vital.getPatient() == null || vital.getPatient().getId() == null) {
            throw new IllegalArgumentException("Patient is required for PatientVital.");
        }
        patientRepository.findById(vital.getPatient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Patient with ID " + vital.getPatient().getId() + " not found."));

        if (vital.getAppointment() == null || vital.getAppointment().getAppointmentId() == null) {
            throw new IllegalArgumentException("Appointment is required for PatientVital.");
        }
        appointmentRepository.findById(vital.getAppointment().getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment with ID " + vital.getAppointment().getAppointmentId() + " not found."));

        if (vital.getVitalType() == null || vital.getVitalType().getTypeId() == null) {
            throw new IllegalArgumentException("VitalType is required for PatientVital.");
        }
        vitalTypeRepository.findById(vital.getVitalType().getTypeId())
                .orElseThrow(() -> new IllegalArgumentException("VitalType with ID " + vital.getVitalType().getTypeId() + " not found."));

        if (vital.getRecordedBy() != null && vital.getRecordedBy().getId() != null) {
            userRepository.findById(vital.getRecordedBy().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User (recordedBy) with ID " + vital.getRecordedBy().getId() + " not found."));
        }

        if (vital.getVitalValue() == null || vital.getVitalValue().isBlank()) {
            throw new IllegalArgumentException("Vital value cannot be empty.");
        }
    }

    private PatientVitalDTO convertToDTO(PatientVital patientVital) {
        PatientVitalDTO dto = new PatientVitalDTO();
        dto.setReadingId(patientVital.getReadingId());
        dto.setPatientId(patientVital.getPatient().getId());
        dto.setAppointmentId(patientVital.getAppointment().getAppointmentId());
        dto.setTypeId(patientVital.getVitalType().getTypeId());
        dto.setVitalValue(patientVital.getVitalValue());
        dto.setRecordedAt(patientVital.getRecordedAt());
        dto.setRecordedById(patientVital.getRecordedBy() != null ? patientVital.getRecordedBy().getId() : null);


        dto.setVitalTypeName(patientVital.getVitalType().getVitalName());
        dto.setVitalTypeUnit(patientVital.getVitalType().getUnit());
        dto.setRecordedByName(patientVital.getRecordedBy() != null ? patientVital.getRecordedBy().getUsername() : null);

        return dto;
    }
}