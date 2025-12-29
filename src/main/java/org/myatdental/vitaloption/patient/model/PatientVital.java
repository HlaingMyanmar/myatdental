package org.myatdental.vitaloption.patient.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.myatdental.appointmentoptions.model.Appointment;
import org.myatdental.authoption.useroptions.model.User;
import org.myatdental.patientoption.model.Patient;
import org.myatdental.vitaloption.type.model.VitalType;


import java.time.LocalDateTime;

@Entity
@Table(name = "patient_vitals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientVital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer readingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private VitalType vitalType;

    @Column(name = "vital_value", nullable = false, length = 50)
    private String vitalValue;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by")
    private User recordedBy;
}