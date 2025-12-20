package org.myatdental.treatmentoptions.service;



import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentoptions.dto.TreatmentDTO;

import org.myatdental.treatmentcategoriesoptions.model.TreatmentCategories;
import org.myatdental.treatmentcategoriesoptions.repository.TreatmentCategoriesRepository;
import org.myatdental.treatmentoptions.model.Treatments;
import org.myatdental.treatmentoptions.repository.TreatmentsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentService {

    private final TreatmentsRepository treatmentsRepository;
    private final TreatmentCategoriesRepository categoriesRepository;

    @Transactional(readOnly = true)
    public List<TreatmentDTO> getAllTreatments() {
        return treatmentsRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TreatmentDTO getTreatmentById(Integer id) {
        Treatments treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found with id: " + id));
        return convertToDTO(treatment);
    }

    @Transactional
    public TreatmentDTO createTreatment(TreatmentDTO dto) {
        if (treatmentsRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Treatment code already exists: " + dto.getCode());
        }

        TreatmentCategories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        Treatments treatment = convertToEntity(dto, category);
        Treatments savedTreatment = treatmentsRepository.save(treatment);
        return convertToDTO(savedTreatment);
    }

    @Transactional
    public TreatmentDTO updateTreatment(Integer id, TreatmentDTO dto) {
        Treatments treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found with id: " + id));

        if (!treatment.getCode().equals(dto.getCode()) && treatmentsRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Treatment code already exists: " + dto.getCode());
        }

        TreatmentCategories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        treatment.setCode(dto.getCode());
        treatment.setDescription(dto.getDescription());
        treatment.setStandardPrice(dto.getStandardPrice());
        treatment.setCategory(category);
        treatment.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : treatment.getIsActive());

        Treatments updatedTreatment = treatmentsRepository.save(treatment);
        return convertToDTO(updatedTreatment);
    }

    @Transactional
    public void deleteTreatment(Integer id) {
        if (!treatmentsRepository.existsById(id)) {
            throw new RuntimeException("Treatment not found with id: " + id);
        }
        treatmentsRepository.deleteById(id);
    }

    private TreatmentDTO convertToDTO(Treatments treatment) {
        TreatmentDTO dto = new TreatmentDTO();
        dto.setTreatmentId(treatment.getTreatmentId());
        dto.setCode(treatment.getCode());
        dto.setDescription(treatment.getDescription());
        dto.setStandardPrice(treatment.getStandardPrice());
        dto.setCategoryId(treatment.getCategory().getCategory_id());
        dto.setIsActive(treatment.getIsActive());
        return dto;
    }

    private Treatments convertToEntity(TreatmentDTO dto, TreatmentCategories category) {
        Treatments treatment = new Treatments();
        treatment.setCode(dto.getCode());
        treatment.setDescription(dto.getDescription());
        treatment.setStandardPrice(dto.getStandardPrice());
        treatment.setCategory(category);
        treatment.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return treatment;
    }
}
