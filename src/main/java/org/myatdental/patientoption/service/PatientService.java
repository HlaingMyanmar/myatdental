package org.myatdental.patientoption.service;
import lombok.RequiredArgsConstructor;
import org.myatdental.patientoption.dto.PatientDTO;
import org.myatdental.patientoption.gender.Gender;
import org.myatdental.patientoption.respository.PatientRepository;
import org.myatdental.patientoption.model.Patient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientDTO getPatientById(Integer id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        return convertToDTO(patient);
    }

    @Transactional
    public PatientDTO createPatient(PatientDTO dto) {
        if (patientRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Patient code already exists: " + dto.getCode());
        }
        Patient patient = convertToEntity(dto);
        Patient savedPatient = patientRepository.save(patient);
        return convertToDTO(savedPatient);
    }

    @Transactional
    public PatientDTO updatePatient(Integer id, PatientDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));


        if (!patient.getCode().equals(dto.getCode()) && patientRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Patient code already exists: " + dto.getCode());
        }


        updateEntityFromDTO(patient, dto);

        Patient updatedPatient = patientRepository.save(patient);
        return convertToDTO(updatedPatient);
    }

    @Transactional
    public void deletePatient(Integer id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    private void updateEntityFromDTO(Patient patient, PatientDTO dto) {
        patient.setCode(dto.getCode());
        patient.setName(dto.getName());
        patient.setPhone(dto.getPhone());
        patient.setDob(dto.getDob());
        if (dto.getGender() != null) {
            patient.setGender(Gender.valueOf(dto.getGender()));
        }
        patient.setMedicalHistory(dto.getMedHist());
        patient.setTownship(dto.getTownship());
        patient.setAddress(dto.getAddress());
    }

    private Patient convertToEntity(PatientDTO dto) {
        Patient patient = new Patient();
        updateEntityFromDTO(patient, dto);
        return patient;
    }

    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setCode(patient.getCode());
        dto.setName(patient.getName());
        dto.setPhone(patient.getPhone());
        dto.setDob(patient.getDob());
        if (patient.getGender() != null) {
            dto.setGender(patient.getGender().name());
        }
        dto.setMedHist(patient.getMedicalHistory());
        dto.setTownship(patient.getTownship());
        dto.setAddress(patient.getAddress());
        return dto;
    }
}