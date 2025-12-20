package org.myatdental.treatmentcategoriesoptions.repository;

import org.myatdental.treatmentcategoriesoptions.model.TreatmentCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TreatmentCategoriesRepository extends JpaRepository<TreatmentCategories, Integer> {

    Optional<TreatmentCategories> findByName(String name);

    boolean existsByName(String name);


}

