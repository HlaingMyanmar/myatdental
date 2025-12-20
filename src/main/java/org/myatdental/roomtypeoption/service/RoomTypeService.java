package org.myatdental.roomtypeoption.service;
import lombok.RequiredArgsConstructor;
import org.myatdental.roomtypeoption.dto.RoomTypeDTO;
import org.myatdental.roomtypeoption.model.RoomType;
import org.myatdental.roomtypeoption.respostory.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    // 🔹 Get All
    @Transactional(readOnly = true)
    public List<RoomTypeDTO> getAllRoomTypes() {
        return roomTypeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Get By ID
    @Transactional(readOnly = true)
    public RoomTypeDTO getRoomTypeById(Integer id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room type not found with id: " + id));
        return convertToDTO(roomType);
    }

    // 🔹 Create
    @Transactional
    public RoomTypeDTO createRoomType(RoomTypeDTO dto) {

        if (roomTypeRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Room type already exists: " + dto.getName());
        }

        RoomType roomType = convertToEntity(dto);
        RoomType saved = roomTypeRepository.save(roomType);
        return convertToDTO(saved);
    }

    // 🔹 Update
    @Transactional
    public RoomTypeDTO updateRoomType(Integer id, RoomTypeDTO dto) {

        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room type not found with id: " + id));

        if (!roomType.getName().equals(dto.getName())
                && roomTypeRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Room type already exists: " + dto.getName());
        }

        roomType.setName(dto.getName());
        roomType.setDescription(dto.getDescription());

        return convertToDTO(roomTypeRepository.save(roomType));
    }

    // 🔹 Delete
    @Transactional
    public void deleteRoomType(Integer id) {
        if (!roomTypeRepository.existsById(id)) {
            throw new RuntimeException("Room type not found with id: " + id);
        }
        roomTypeRepository.deleteById(id);
    }

    // =========================
    // 🔁 Converter Methods
    // =========================

    private RoomTypeDTO convertToDTO(RoomType roomType) {
        RoomTypeDTO dto = new RoomTypeDTO();
        dto.setId(roomType.getId());
        dto.setName(roomType.getName());
        dto.setDescription(roomType.getDescription());
        return dto;
    }

    private RoomType convertToEntity(RoomTypeDTO dto) {
        RoomType roomType = new RoomType();
        roomType.setName(dto.getName());
        roomType.setDescription(dto.getDescription());
        return roomType;
    }
}

