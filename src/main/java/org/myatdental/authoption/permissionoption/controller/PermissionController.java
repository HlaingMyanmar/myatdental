package org.myatdental.authoption.permissionoption.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.authoption.permissionoption.dto.PermissionDTO;
import org.myatdental.authoption.permissionoption.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String PERMISSION_TOPIC = "/topic/permissions";

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO dto) {
        PermissionDTO created = permissionService.createPermission(dto);
        messagingTemplate.convertAndSend(PERMISSION_TOPIC, "PERMISSION_CREATED");
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable Long id,
                                                          @RequestBody PermissionDTO dto) {
        PermissionDTO updated = permissionService.updatePermission(id, dto);
        messagingTemplate.convertAndSend(PERMISSION_TOPIC, "PERMISSION_UPDATED");
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        messagingTemplate.convertAndSend(PERMISSION_TOPIC, "PERMISSION_DELETED");
        return ResponseEntity.noContent().build();
    }

}
