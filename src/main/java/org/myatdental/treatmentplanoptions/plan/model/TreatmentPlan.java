package org.myatdental.treatmentplanoptions.plan.model;

import jakarta.persistence.*;
import lombok.*;
import org.myatdental.patientplanoption.model.PatientPlan;
import org.myatdental.treatmentplanoptions.item.model.TreatmentPlanItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "treatment_plans")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer planId;

    @Column(length = 10, nullable = false, unique = true)
    private String code;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(name = "is_template")
    private Boolean isTemplate = false;

    @Column(name = "total_cost", precision = 12, scale = 2)
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Column(name = "installments_allowed")
    private Boolean installmentsAllowed = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TreatmentPlanItem> items = new ArrayList<>();



    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.isActive == null) this.isActive = true;
    }


    public void addItem(TreatmentPlanItem item) {
        items.add(item);
        item.setPlan(this);
    }

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<PatientPlan> patientPlans;
}