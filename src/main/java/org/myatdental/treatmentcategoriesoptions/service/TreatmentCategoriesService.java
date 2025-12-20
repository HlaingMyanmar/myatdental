package org.myatdental.treatmentcategoriesoptions.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentcategoriesoptions.dto.TreatmentCategoriesDTO;
import org.myatdental.treatmentcategoriesoptions.model.TreatmentCategories;
import org.myatdental.treatmentcategoriesoptions.repository.TreatmentCategoriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentCategoriesService {

    private final TreatmentCategoriesRepository treatmentCategoriesRepository;

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

    private TreatmentCategoriesDTO convertToDTO(TreatmentCategories category) {
        TreatmentCategoriesDTO dto = new TreatmentCategoriesDTO();
        dto.setCategoryId(category.getCategory_id());
        dto.setName(category.getName());
        return dto;
    }

    private TreatmentCategories convertToEntity(TreatmentCategoriesDTO dto) {
        TreatmentCategories category = new TreatmentCategories();
        category.setName(dto.getName());
        return category;
    }
}
