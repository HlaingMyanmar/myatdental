package org.myatdental.vitaloption.type.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "vital_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VitalType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;

    @Column(name = "vital_name", nullable = false, unique = true, length = 50)
    private String vitalName;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}