package org.myatdental.treatmentcategoriesoptions.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentcategoriesoptions.dto.TreatmentCategoriesDTO;
import org.myatdental.treatmentcategoriesoptions.model.TreatmentCategories;
import org.myatdental.treatmentcategoriesoptions.repository.TreatmentCategoriesRepository;
import org.myatdental.treatmentoptions.dto.TreatmentDTO;
import org.myatdental.treatmentoptions.model.Treatments;
import org.myatdental.treatmentoptions.repository.TreatmentsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentCategoriesService {

    private final TreatmentCategoriesRepository treatmentCategoriesRepository;
    private final TreatmentsRepository treatmentRepository;

    @Transactional(readOnly = true)
    public List<TreatmentCategoriesDTO> getAllCategories() {
        return treatmentCategoriesRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TreatmentCategoriesDTO getCategoryById(Integer id) {
        TreatmentCategories category = treatmentCategoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToDTO(category);
    }

    @Transactional
    public TreatmentCategoriesDTO createCategory(TreatmentCategoriesDTO dto) {
        if (treatmentCategoriesRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Category name already exists: " + dto.getName());
        }

        TreatmentCategories category = convertToEntity(dto);
        TreatmentCategories savedCategory = treatmentCategoriesRepository.save(category);
        return convertToDTO(savedCategory);
    }

    @Transactional
    public TreatmentCategoriesDTO updateCategory(Integer id, TreatmentCategoriesDTO dto) {
        TreatmentCategories category = treatmentCategoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (!category.getName().equals(dto.getName()) && treatmentCategoriesRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Category name already exists: " + dto.getName());
        }

        category.setName(dto.getName());

        TreatmentCategories updatedCategory = treatmentCategoriesRepository.save(category);
        return convertToDTO(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        if (!treatmentCategoriesRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        treatmentCategoriesRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TreatmentDTO> getTreatmentsByCategoryId(Integer categoryId) {
        if (!treatmentCategoriesRepository.existsById(categoryId)) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
        List<Treatments> treatments = treatmentRepository.findAllByCategoryCategoryId(categoryId);
        return treatments.stream()
                .map(this::convertToTreatmentDTO)
                .collect(Collectors.toList());
    }

    private TreatmentCategoriesDTO convertToDTO(TreatmentCategories category) {
        TreatmentCategoriesDTO dto = new TreatmentCategoriesDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());
        return dto;
    }

    private TreatmentCategories convertToEntity(TreatmentCategoriesDTO dto) {
        TreatmentCategories category = new TreatmentCategories();
        category.setName(dto.getName());
        return category;
    }
    private TreatmentDTO convertToTreatmentDTO(Treatments treatment) {
        TreatmentDTO dto = new TreatmentDTO();
        dto.setTreatmentId(treatment.getTreatmentId());
        dto.setCode(treatment.getCode());
        dto.setDescription(treatment.getDescription());
        dto.setStandardPrice(treatment.getStandardPrice());
        dto.setCategoryId(treatment.getCategory().getCategoryId());
        dto.setIsActive(treatment.getIsActive());
        return dto;
    }

    @Transactional
    public TreatmentDTO assignTreatmentToCategory(Integer categoryId, TreatmentDTO treatmentDto) {

        TreatmentCategories category = treatmentCategoriesRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Treatments treatment = new Treatments();
        treatment.setCode(treatmentDto.getCode());
        treatment.setDescription(treatmentDto.getDescription());
        treatment.setStandardPrice(treatmentDto.getStandardPrice());
        treatment.setIsActive(true);
        category.addTreatment(treatment);
        treatmentCategoriesRepository.save(category);
        return convertToTreatmentDTO(treatment);
    }
    @Transactional
    public void removeTreatmentFromCategory(Integer categoryId, Integer treatmentId) {
        TreatmentCategories category =  treatmentCategoriesRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Treatments treatment = category.getTreatments().stream()
                .filter(t -> t.getTreatmentId().equals(treatmentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Treatment not found in this category"));
        category.removeTreatment(treatment);
        treatmentCategoriesRepository.save(category);
    }
}
