package org.myatdental.treatmentcategoriesoptions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "treatment_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer category_id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;
}
