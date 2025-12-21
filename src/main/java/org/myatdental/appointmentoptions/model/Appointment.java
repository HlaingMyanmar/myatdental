package org.myatdental.appointmentoptions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myatdental.appointmentoptions.status.AppointmentStatus;
import org.myatdental.dentistoptions.model.Dentist;
import org.myatdental.patientoption.model.Patient;
import org.myatdental.roomoptions.model.Room;
import org.myatdental.treatmentrecord.model.TreatmentRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer id;

    @Column(length = 10, nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id", nullable = false)
    private Dentist dentist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "token_number")
    private Integer tokenNumber;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes = 30;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.Scheduled;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "patient_plan_id")
    private Integer patientPlanId;

    @Column(name = "is_follow_up")
    private Boolean isFollowUp = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_appointment_id")
    private Appointment previousAppointment;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.appointmentDate == null) this.appointmentDate = LocalDate.now();


    }

    @OneToMany(
            mappedBy = "appointment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TreatmentRecord> treatmentRecords;

}
