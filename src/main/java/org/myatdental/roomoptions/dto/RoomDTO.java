package org.myatdental.roomoptions.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RoomDTO {

    private Integer roomId;

    @NotBlank(message = "Room name is required")
    @Size(max = 20, message = "Room name must not exceed 20 characters")
    private String roomName;

    @NotNull(message = "Room type is required")
    private Integer typeId;

    private Boolean isActive = true;

    private String notes;
}
