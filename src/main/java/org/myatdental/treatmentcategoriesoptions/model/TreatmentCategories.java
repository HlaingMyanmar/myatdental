package org.myatdental.treatmentcategoriesoptions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myatdental.treatmentoptions.model.Treatments;

import java.util.ArrayList;
import java.util.List;

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
    private Integer categoryId;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Treatments> treatments = new ArrayList<>();

    public void addTreatment(Treatments treatment) {
        treatments.add(treatment);
        treatment.setCategory(this);
    }
    public void removeTreatment(Treatments treatment) {
        treatments.remove(treatment);
        treatment.setCategory(null);
    }
}
