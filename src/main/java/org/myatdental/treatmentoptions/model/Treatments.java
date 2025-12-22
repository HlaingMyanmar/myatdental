package org.myatdental.treatmentoptions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myatdental.treatmentcategoriesoptions.model.TreatmentCategories;
import org.myatdental.treatmentrecord.model.TreatmentRecord;


import java.math.BigDecimal;
import java.util.List;

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
    private Integer treatmentId;

    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "standard_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal standardPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private TreatmentCategories category;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @OneToMany(
            mappedBy = "treatment",
            cascade = CascadeType.ALL
    )
    private List<TreatmentRecord> treatmentRecords;


}

