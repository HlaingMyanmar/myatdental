package org.myatdental.additionalchargesoptions.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdditionalChargesDTO {

    private Integer charge_id;
    private String name;
    private String description;
    private BigDecimal default_price;
    private Boolean is_active;
    private LocalDateTime created_at;
}
