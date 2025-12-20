package org.myatdental.dentistoptions.repository;

import org.myatdental.dentistoptions.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DentistRepository extends JpaRepository<Dentist, Long> {


    Optional<Dentist> findByCode(String code);
    Optional<Dentist> findByEmail(String email);
    boolean existsByCode(String code);
    boolean existsByEmail(String email);
}