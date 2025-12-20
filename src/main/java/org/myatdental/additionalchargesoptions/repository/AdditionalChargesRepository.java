package org.myatdental.additionalchargesoptions.repository;

import org.myatdental.additionalchargesoptions.model.AdditionalCharges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdditionalChargesRepository extends JpaRepository<AdditionalCharges, Integer> {

    // Name နဲ့ search
    Optional<AdditionalCharges> findByName(String name);

    // Name already exists check
    boolean existsByName(String name);

    // Active charges only
    Optional<AdditionalCharges> findByIs_activeTrue();

    // Active check by name
    boolean existsByNameAndIs_activeTrue(String name);
}
