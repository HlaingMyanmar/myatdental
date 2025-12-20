package org.myatdental.dentistoptions.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DentistDTO {
    private Long id;
    private String code;
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private Boolean isActive;
    private LocalDate joinDate;
    private LocalDateTime createdAt;
}