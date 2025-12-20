package org.myatdental.treatmentplanoptions.item.repository;

import org.myatdental.treatmentplanoptions.item.model.TreatmentPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentPlanItemRepository extends JpaRepository<TreatmentPlanItem, Integer> {

    List<TreatmentPlanItem> findByPlanPlanId(Integer planId);


    List<TreatmentPlanItem> findByTreatmentTreatmentId(Integer treatmentId);
}