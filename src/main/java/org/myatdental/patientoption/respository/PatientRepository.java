package org.myatdental.patientoption.respository;
import org.myatdental.patientoption.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findByCode(String code);

    boolean existsByCode(String code);
}

