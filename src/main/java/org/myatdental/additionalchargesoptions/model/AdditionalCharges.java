package org.myatdental.additionalchargesoptions.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myatdental.inoviceoptions.invoiceadditioanal.model.InvoiceAdditionalCharge;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private Integer chargeId;

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "default_price", precision = 12, scale = 2)
    private BigDecimal defaultPrice;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "charge", fetch = FetchType.LAZY)
    private List<InvoiceAdditionalCharge> invoiceAdditionalCharges = new ArrayList<>();
}
