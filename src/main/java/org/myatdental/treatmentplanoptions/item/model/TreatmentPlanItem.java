package org.myatdental.treatmentplanoptions.item.model;

import jakarta.persistence.*;
import lombok.*;
import org.myatdental.treatmentoptions.model.Treatments;
import org.myatdental.treatmentplanoptions.plan.model.TreatmentPlan;

import java.math.BigDecimal;

@Entity
@Table(name = "treatment_plan_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TreatmentPlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private TreatmentPlan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatments treatment;

    @Column(name = "estimated_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal estimatedPrice;
}