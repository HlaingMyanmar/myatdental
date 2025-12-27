package org.myatdental.authoption.useroptions.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.authoption.useroptions.dto.UserDTO;
import org.myatdental.authoption.useroptions.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate; // WebSocket အတွက်
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate; // WebSocket Template

    // WebSocket Topic လမ်းကြောင်း
    private static final String USER_TOPIC = "/topic/users";

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        UserDTO createdUser = userService.createUser(dto);
        messagingTemplate.convertAndSend(USER_TOPIC, "USER_CREATED");
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        UserDTO updatedUser = userService.updateUser(id, dto);
        messagingTemplate.convertAndSend(USER_TOPIC, "USER_UPDATED");
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        messagingTemplate.convertAndSend(USER_TOPIC, "USER_DELETED");
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<UserDTO> toggleUserStatus(@PathVariable Long id) {
        UserDTO updated = userService.toggleUserStatus(id);
        messagingTemplate.convertAndSend(USER_TOPIC, "USER_STATUS_TOGGLED");
        return ResponseEntity.ok(updated);
    }
}