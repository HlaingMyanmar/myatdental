package org.myatdental.treatmentplanoptions.plan.repository;

import org.myatdental.treatmentplanoptions.plan.model.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Integer> {


    List<TreatmentPlan> findByIsTemplateTrue();


    boolean existsByCode(String code);


    Optional<TreatmentPlan> findByCode(String code);
}