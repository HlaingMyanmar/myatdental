package org.myatdental.paymentmethodoptions.respositry;
import org.myatdental.paymentmethodoptions.model.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethods, Integer> {

    Optional<PaymentMethods> findByName(String name);

    boolean existsByName(String name);
}
