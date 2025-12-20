package org.myatdental.treatmentcategoriesoptions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.myatdental.treatmentoptions.dto.TreatmentDTO;

import java.util.List;

@Data
public class TreatmentCategoriesDTO {

    private Integer categoryId;

    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name must not exceed 50 characters")
    private String name;

    private List<TreatmentDTO> treatments;
}