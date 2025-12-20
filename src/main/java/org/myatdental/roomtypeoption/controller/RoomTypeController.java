package org.myatdental.roomtypeoption.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.roomtypeoption.dto.RoomTypeDTO;
import org.myatdental.roomtypeoption.service.RoomTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    // 🔹 Get All
    @GetMapping
    public ResponseEntity<List<RoomTypeDTO>> getAllRoomTypes() {
        return ResponseEntity.ok(roomTypeService.getAllRoomTypes());
    }

    // 🔹 Get By ID
    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDTO> getRoomTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomTypeService.getRoomTypeById(id));
    }

    // 🔹 Create
    @PostMapping
    public ResponseEntity<RoomTypeDTO> createRoomType(
            @Valid @RequestBody RoomTypeDTO dto
    ) {
        return ResponseEntity.ok(roomTypeService.createRoomType(dto));
    }

    // 🔹 Update
    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeDTO> updateRoomType(
            @PathVariable Integer id,
            @Valid @RequestBody RoomTypeDTO dto
    ) {
        return ResponseEntity.ok(roomTypeService.updateRoomType(id, dto));
    }

    // 🔹 Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Integer id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }
}

