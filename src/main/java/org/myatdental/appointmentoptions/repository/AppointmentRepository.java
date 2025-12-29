package org.myatdental.appointmentoptions.repository;

import org.myatdental.appointmentoptions.model.Appointment;
import org.myatdental.appointmentoptions.status.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("SELECT MAX(a.tokenNumber) FROM Appointment a WHERE a.appointmentDate = :date")
    Integer findMaxTokenByDate(@Param("date") LocalDate date);

    List<Appointment> findByAppointmentDateOrderByTokenNumberAsc(LocalDate date);

    List<Appointment> findByAppointmentDateBetween(LocalDate start, LocalDate end);

    // ဆရာဝန် အချိန်ထပ်မထပ်စစ်ရန် (Long ပြန်ပေးရန်ပြင်ထားသည်)
    @Query(value = "SELECT COUNT(*) FROM appointments a WHERE a.dentist_id = :dentistId " +
            "AND a.appointment_date = :date AND a.status NOT IN ('Cancelled') " +
            "AND (:currentId IS NULL OR a.appointment_id != :currentId) " +
            "AND ((a.appointment_time < :endTime) AND (ADDTIME(a.appointment_time, SEC_TO_TIME(a.duration_minutes * 60)) > :startTime))",
            nativeQuery = true)
    Long countOverlappingDentistAppt(
            @Param("dentistId") Integer dentistId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("currentId") Integer currentId);

    // အခန်း အချိန်ထပ်မထပ်စစ်ရန်
    @Query(value = "SELECT COUNT(*) FROM appointments a WHERE a.room_id = :roomId " +
            "AND a.appointment_date = :date AND a.status NOT IN ('Cancelled') " +
            "AND (:currentId IS NULL OR a.appointment_id != :currentId) " +
            "AND ((a.appointment_time < :endTime) AND (ADDTIME(a.appointment_time, SEC_TO_TIME(a.duration_minutes * 60)) > :startTime))",
            nativeQuery = true)
    Long countOverlappingRoomAppt(
            @Param("roomId") Integer roomId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("currentId") Integer currentId);

    List<Appointment> findByDentist_Id(Long dentistId);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByDentist_IdAndStatusInOrderByTokenNumberAsc(Long dentistId, List<AppointmentStatus> statuses);
}