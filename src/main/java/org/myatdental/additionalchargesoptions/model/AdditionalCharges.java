package org.myatdental.additionalchargesoptions.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "additional_charges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalCharges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charge_id")
    private Integer charge_id;

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "default_price", precision = 12, scale = 2)
    private BigDecimal default_price;

    @Column(name = "is_active")
    private Boolean is_active = true;
}
