package org.myatdental.roomtypeoption.service;
import lombok.RequiredArgsConstructor;
import org.myatdental.roomoptions.dto.RoomDTO;
import org.myatdental.roomoptions.model.Room;
import org.myatdental.roomoptions.repository.RoomRepository;
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
    private final RoomRepository roomRepository;

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
    @Transactional(readOnly = true)
    public List<RoomDTO> getRoomsByRoomTypeId(Integer typeId) {
        // ၁။ Room Type ရှိမရှိ အရင်စစ်မယ်
        if (!roomTypeRepository.existsById(typeId)) {
            throw new RuntimeException("Room Type not found with id: " + typeId);
        }


        List<Room> rooms = roomRepository.findAllByRoomType_Id(typeId);

        // ၃။ Entity list ကို DTO list ပြောင်းပြီး ပြန်ပေးမယ်
        return rooms.stream()
                .map(this::convertToRoomDTO)
                .collect(Collectors.toList());
    }

    // Helper method: Room Entity to RoomDTO
    private RoomDTO convertToRoomDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setRoomId(room.getRoomId());
        dto.setRoomName(room.getRoomName());
        dto.setTypeId(room.getRoomType().getId());
        dto.setIsActive(room.getIsActive());
        dto.setNotes(room.getNotes());
        return dto;
    }
}

