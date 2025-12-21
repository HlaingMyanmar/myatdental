package org.myatdental.patientplanoption.service;
import lombok.RequiredArgsConstructor;
import org.myatdental.patientoption.model.Patient;
import org.myatdental.patientoption.respository.PatientRepository;
import org.myatdental.patientplanoption.dto.PatientPlanDTO;
import org.myatdental.patientplanoption.model.PatientPlan;
import org.myatdental.patientplanoption.respository.PatientPlanRepository;
import org.myatdental.treatmentplanoptions.plan.model.TreatmentPlan;
import org.myatdental.treatmentplanoptions.plan.repository.TreatmentPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientPlanService {

    private final PatientPlanRepository patientPlanRepository;
    private final PatientRepository patientRepository;
    private final TreatmentPlanRepository treatmentPlanRepository;

    @Transactional(readOnly = true)
    public List<PatientPlanDTO> getAllPatientPlans() {
        return patientPlanRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientPlanDTO getPatientPlanById(Long id) {
        PatientPlan pp = patientPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientPlan not found with id: " + id));
        return convertToDTO(pp);
    }

    @Transactional
    public PatientPlanDTO createPatientPlan(PatientPlanDTO dto) {

        Patient patient = patientRepository.findById(Math.toIntExact(dto.getPatientId()))
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + dto.getPatientId()));

        // TreatmentPlan existence check
        TreatmentPlan plan = treatmentPlanRepository.findById(Math.toIntExact(dto.getPlanId()))
                .orElseThrow(() -> new RuntimeException("TreatmentPlan not found with id: " + dto.getPlanId()));

        // Unique constraint check (patient + plan)
        if (patientPlanRepository.existsByPatientAndPlan(patient, plan)) {
            throw new RuntimeException("Patient already assigned to this treatment plan.");
        }

        PatientPlan pp = new PatientPlan();
        pp.setPatient(patient);
        pp.setPlan(plan);
        pp.setActualTotalCost(dto.getActualTotalCost() != null && dto.getActualTotalCost().compareTo(BigDecimal.ZERO) != 0
                ? dto.getActualTotalCost()
                : BigDecimal.ZERO);
        pp.setAssignedDate(dto.getAssignedDate() != null ? dto.getAssignedDate() : java.time.LocalDate.now());

        PatientPlan saved = patientPlanRepository.save(pp);
        return convertToDTO(saved);
    }

    @Transactional
    public PatientPlanDTO updatePatientPlan(Long id, PatientPlanDTO dto) {
        PatientPlan pp = patientPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientPlan not found with id: " + id));

        if (dto.getActualTotalCost() != null) {
            pp.setActualTotalCost(dto.getActualTotalCost().compareTo(BigDecimal.ZERO) != 0
                    ? dto.getActualTotalCost()
                    : BigDecimal.ZERO);
        }
        if (dto.getAssignedDate() != null) {
            pp.setAssignedDate(dto.getAssignedDate());
        }

        PatientPlan updated = patientPlanRepository.save(pp);
        return convertToDTO(updated);
    }

    @Transactional
    public void deletePatientPlan(Long id) {
        if (!patientPlanRepository.existsById(id)) {
            throw new RuntimeException("PatientPlan not found with id: " + id);
        }
        patientPlanRepository.deleteById(id);
    }

    private PatientPlanDTO convertToDTO(PatientPlan pp) {
        PatientPlanDTO dto = new PatientPlanDTO();
        dto.setId(Long.valueOf(pp.getId()));
        dto.setPatientId(Long.valueOf(pp.getPatient().getId()));
        dto.setPlanId(Long.valueOf(pp.getPlan().getPlanId()));
        dto.setActualTotalCost(pp.getActualTotalCost() != null ? pp.getActualTotalCost() : BigDecimal.ZERO);
        dto.setAssignedDate(pp.getAssignedDate());
        return dto;
    }
}
