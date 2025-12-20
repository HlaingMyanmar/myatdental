package org.myatdental.roomoptions.repository;

import org.myatdental.roomoptions.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    Optional<Room> findByRoomName(String roomName);

    boolean existsByRoomName(String roomName);

    List<Room> findAllByRoomType_Id(Integer typeId);
}

