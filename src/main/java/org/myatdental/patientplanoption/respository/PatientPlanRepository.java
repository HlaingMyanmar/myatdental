package org.myatdental.patientplanoption.respository;

import org.myatdental.patientplanoption.model.PatientPlan;
import org.myatdental.patientoption.model.Patient;
import org.myatdental.treatmentplanoptions.plan.model.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientPlanRepository extends JpaRepository<PatientPlan, Long> {

    // Unique check for patient + plan
    boolean existsByPatientAndPlan(Patient patient, TreatmentPlan plan);
}
