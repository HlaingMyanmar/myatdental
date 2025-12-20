package org.myatdental.roomoptions;
import lombok.RequiredArgsConstructor;
import org.myatdental.roomtypeoption.model.RoomType;
import org.myatdental.roomtypeoption.respostory.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Transactional(readOnly = true)
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomDTO getRoomById(Integer roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        return convertToDTO(room);
    }

    @Transactional
    public RoomDTO createRoom(RoomDTO dto) {
        // RoomType ကို verify
        RoomType type = roomTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("RoomType not found with id: " + dto.getTypeId()));

        Room room = convertToEntity(dto, type);
        Room savedRoom = roomRepository.save(room);
        return convertToDTO(savedRoom);
    }

    @Transactional
    public RoomDTO updateRoom(Integer roomId, RoomDTO dto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));

        RoomType type = roomTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("RoomType not found with id: " + dto.getTypeId()));

        room.setRoomName(dto.getRoomName());
        room.setRoomType(type);
        room.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : room.getIsActive());
        room.setNotes(dto.getNotes());

        Room updatedRoom = roomRepository.save(room);
        return convertToDTO(updatedRoom);
    }

    @Transactional
    public void deleteRoom(Integer roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new RuntimeException("Room not found with id: " + roomId);
        }
        roomRepository.deleteById(roomId);
    }

    @Transactional
    public RoomDTO toggleRoomStatus(Integer roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));

        room.setIsActive(!room.getIsActive());
        return convertToDTO(roomRepository.save(room));
    }

    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setRoomId(room.getRoomId());
        dto.setRoomName(room.getRoomName());
        dto.setTypeId(room.getRoomType().getId());
        dto.setIsActive(room.getIsActive());
        dto.setNotes(room.getNotes());
        return dto;
    }

    private Room convertToEntity(RoomDTO dto, RoomType type) {
        Room room = new Room();
        room.setRoomName(dto.getRoomName());
        room.setRoomType(type);
        room.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        room.setNotes(dto.getNotes());
        return room;
    }
}

