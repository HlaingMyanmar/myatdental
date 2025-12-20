package org.myatdental.roomtypeoption.respostory;
import org.myatdental.roomoptions.model.Room;
import org.myatdental.roomtypeoption.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    Optional<RoomType> findByName(String name);

    boolean existsByName(String name);

}
