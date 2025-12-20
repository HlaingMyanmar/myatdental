package org.myatdental.roomtypeoption.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoomTypeDTO {

    private Integer id;

    @NotBlank(message = "Room type name is required")
    @Size(max = 30, message = "Room type name must not exceed 30 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}

