package org.myatdental.authoption.useroptions.repository;

import org.myatdental.authoption.useroptions.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Username (သို့) Email တစ်ခုခုနဲ့ ရှာရန်
    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}