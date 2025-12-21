package org.myatdental.patientplanoption.model;

import jakarta.persistence.*;
import lombok.*;
import org.myatdental.patientoption.model.Patient;
import org.myatdental.treatmentplanoptions.plan.model.TreatmentPlan;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "patient_plans",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"patient_id", "plan_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pp_id")
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private TreatmentPlan plan;

    @Column(name = "actual_total_cost", precision = 12, scale = 2)
    private BigDecimal actualTotalCost = BigDecimal.ZERO;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    @PrePersist
    protected void onCreate() {
        if (this.assignedDate == null) {
            this.assignedDate = LocalDate.now();
        }
    }
}

