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


    @GetMapping
    public ResponseEntity<List<RoomTypeDTO>> getAllRoomTypes() {
        return ResponseEntity.ok(roomTypeService.getAllRoomTypes());
    }


    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDTO> getRoomTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomTypeService.getRoomTypeById(id));
    }


    @PostMapping
    public ResponseEntity<RoomTypeDTO> createRoomType(
            @Valid @RequestBody RoomTypeDTO dto
    ) {
        return ResponseEntity.ok(roomTypeService.createRoomType(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeDTO> updateRoomType(
            @PathVariable Integer id,
            @Valid @RequestBody RoomTypeDTO dto
    ) {
        return ResponseEntity.ok(roomTypeService.updateRoomType(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Integer id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }
}

