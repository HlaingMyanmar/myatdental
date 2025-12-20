package org.myatdental.additionalchargesoptions.repository;

import org.myatdental.additionalchargesoptions.model.AdditionalCharges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdditionalChargesRepository extends JpaRepository<AdditionalCharges, Long> {


    Optional<AdditionalCharges> findByName(String name);

    boolean existsByName(String name);

    Optional<AdditionalCharges> findByIsActiveTrue();

    boolean existsByNameAndIsActiveTrue(String name);
}
