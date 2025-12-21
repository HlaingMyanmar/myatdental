package org.myatdental.dentistoptions.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DentistDTO {

    private Long id;

    @NotBlank(message = "Dentist code is required")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String code;

    @NotBlank(message = "Dentist name is required")
    @Size(max = 30, message = "Name must not exceed 30 characters")
    private String name;

    @Size(max = 30, message = "Specialization must not exceed 30 characters")
    private String specialization;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{9,15}$", message = "Invalid phone number format")
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    private Boolean isActive;

    private LocalDate joinDate;

    private Long userId;
}