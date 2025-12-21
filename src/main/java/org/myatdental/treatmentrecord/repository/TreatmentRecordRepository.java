package org.myatdental.treatmentrecord.repository;
import org.myatdental.appointmentoptions.model.Appointment;
import org.myatdental.treatmentoptions.model.Treatments;
import org.myatdental.treatmentrecord.model.TreatmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TreatmentRecordRepository extends JpaRepository<TreatmentRecord, Long> {


    List<TreatmentRecord> findByAppointment(Appointment appointment);

    List<TreatmentRecord> findByTreatment(Treatments treatment);


    List<TreatmentRecord> findByAppointment_AppointmentId(Long appointmentId);


    List<TreatmentRecord> findByTreatment_TreatmentId(Long treatmentId);

    List<TreatmentRecord> findByPerformedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}

