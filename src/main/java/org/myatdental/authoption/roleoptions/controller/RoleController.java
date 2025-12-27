package org.myatdental.authoption.roleoptions.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.authoption.roleoptions.dto.RoleDTO;
import org.myatdental.authoption.roleoptions.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoleService roleService;

    private static final String ROLE_TOPIC = "/topic/roles";

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO created = roleService.createRole(roleDTO);
        messagingTemplate.convertAndSend(ROLE_TOPIC, "ROLE_CREATED");
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Integer id, @RequestBody RoleDTO roleDTO) {
        RoleDTO updated = roleService.updateRole(id, roleDTO);
        messagingTemplate.convertAndSend(ROLE_TOPIC, "ROLE_UPDATED");
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        messagingTemplate.convertAndSend(ROLE_TOPIC, "ROLE_DELETED");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> assignPermission(@PathVariable Integer roleId, @PathVariable Long permissionId) {
        roleService.assignPermission(roleId, permissionId);
        messagingTemplate.convertAndSend(ROLE_TOPIC, "PERMISSION_ASSIGNED");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> removePermission(@PathVariable Integer roleId, @PathVariable Long permissionId) {
        roleService.removePermission(roleId, permissionId);
        messagingTemplate.convertAndSend(ROLE_TOPIC, "PERMISSION_REMOVED");
        return ResponseEntity.noContent().build();
    }
}