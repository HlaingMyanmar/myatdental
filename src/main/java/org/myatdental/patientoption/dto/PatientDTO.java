package org.myatdental.patientoption.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.myatdental.patientoption.gender.Gender;

import java.time.LocalDate;

@Data
public class PatientDTO {

    private Integer id;

    @NotBlank(message = "Patient code is required")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String code;

    @NotBlank(message = "Patient name is required")
    @Size(max = 30, message = "Name must not exceed 30 characters")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Size(max = 30, message = "Phone must not exceed 30 characters")
    private String phone;

    private LocalDate dob;

    @NotNull(message = "Gender is required") // null မဖြစ်စေချင်ရင်
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female or Other")
    private String gender;

    private String medHist;

    @Size(max = 30, message = "Township must not exceed 30 characters")
    private String township;

    private String address;
}

