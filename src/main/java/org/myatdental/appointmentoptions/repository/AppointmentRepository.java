package org.myatdental.appointmentoptions.repository;

import org.myatdental.appointmentoptions.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // အဲဒီနေ့အတွက် အမြင့်ဆုံး Token နံပါတ်ကို ရှာရန်
    @Query("SELECT MAX(a.tokenNumber) FROM Appointment a WHERE a.appointmentDate = :date")
    Integer findMaxTokenByDate(@Param("date") LocalDate date);

    // လူနာအလိုက် ရက်ချိန်းများ ပြန်ရှာရန်
    List<Appointment> findByPatientId(Integer patientId);

    // နေ့စဉ် ရက်ချိန်းစာရင်းကို Token အစီအစဉ်အတိုင်း ပြရန်
    List<Appointment> findByAppointmentDateOrderByTokenNumberAsc(LocalDate date);

    // ဆရာဝန် အချိန်ထပ်/မထပ် စစ်ဆေးရန် (Update အတွက် မိမိ ID ကို ချန်လှပ်ထားနိုင်သည်)
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.dentist.id = :dentistId " +
            "AND a.appointmentDate = :date AND a.status NOT IN (org.myatdental.appointmentoptions.status.AppointmentStatus.Cancelled) " +
            "AND (:currentId IS NULL OR a.id != :currentId) " +
            "AND ((a.appointmentTime < :endTime) AND (ADDTIME(a.appointmentTime, SEC_TO_TIME(a.durationMinutes * 60)) > :startTime))")
    boolean existsOverlappingDentistAppt(
            @Param("dentistId") Integer dentistId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("currentId") Integer currentId);

    // အခန်း အချိန်ထပ်/မထပ် စစ်ဆေးရန် (Update အတွက် မိမိ ID ကို ချန်လှပ်ထားနိုင်သည်)
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.room.roomId = :roomId " +
            "AND a.appointmentDate = :date AND a.status NOT IN (org.myatdental.appointmentoptions.status.AppointmentStatus.Cancelled) " +
            "AND (:currentId IS NULL OR a.id != :currentId) " +
            "AND ((a.appointmentTime < :endTime) AND (ADDTIME(a.appointmentTime, SEC_TO_TIME(a.durationMinutes * 60)) > :startTime))")
    boolean existsOverlappingRoomAppt(
            @Param("roomId") Integer roomId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("currentId") Integer currentId);

    // ရက်စွဲအပိုင်းအခြားအလိုက် ရှာရန်
    List<Appointment> findByAppointmentDateBetween(LocalDate start, LocalDate end);
}