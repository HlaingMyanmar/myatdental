package org.myatdental.treatmentrecord.model;

import jakarta.persistence.*;
import lombok.*;
import org.myatdental.appointmentoptions.model.Appointment;
import org.myatdental.treatmentoptions.model.Treatments;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "treatment_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "appointment_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_treatment_record_appointment")
    )
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "treatment_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_treatment_record_treatment")
    )
    private Treatments treatment;

    @Column(name = "tooth_or_site", length = 50)
    private String toothOrSite;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "price_charged", nullable = false, precision = 12, scale = 2)
    private BigDecimal priceCharged;

    @Column(name = "record_notes", columnDefinition = "TEXT")
    private String recordNotes;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @PrePersist
    protected void onCreate() {
        if (performedAt == null) {
            performedAt = LocalDateTime.now();
        }
    }
}

