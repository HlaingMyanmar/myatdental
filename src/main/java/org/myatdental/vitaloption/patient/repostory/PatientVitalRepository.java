package org.myatdental.vitaloption.patient.repostory; // repOsitory ( typo )

import org.myatdental.vitaloption.patient.model.PatientVital;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientVitalRepository extends JpaRepository<PatientVital, Integer> {


    List<PatientVital> findByPatient_Id(Integer patientId);



}