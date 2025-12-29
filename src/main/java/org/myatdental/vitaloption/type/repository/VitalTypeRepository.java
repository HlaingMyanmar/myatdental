package org.myatdental.vitaloption.type.repository;


import org.myatdental.vitaloption.type.model.VitalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VitalTypeRepository extends JpaRepository<VitalType, Integer> {
    Optional<VitalType> findByVitalName(String vitalName);
    List<VitalType> findByIsActiveTrue();
}