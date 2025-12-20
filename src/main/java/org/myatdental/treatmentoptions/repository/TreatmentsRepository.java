package org.myatdental.treatmentoptions.repository;



import org.myatdental.treatmentoptions.model.Treatments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TreatmentsRepository extends JpaRepository<Treatments, Integer> {

    Optional<Treatments> findByCode(String code);

    boolean existsByCode(String code);
}

