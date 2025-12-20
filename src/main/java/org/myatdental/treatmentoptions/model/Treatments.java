package org.myatdental.treatmentoptions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myatdental.treatmentcategoriesoptions.model.TreatmentCategories;

import java.math.BigDecimal;

@Entity
@Table(name = "treatments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Treatments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private Integer treatment_id;

    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "standard_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal standard_price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_treatment_category"))
    private TreatmentCategories category;

    @Column(name = "is_active")
    private Boolean isActive = true;
}

