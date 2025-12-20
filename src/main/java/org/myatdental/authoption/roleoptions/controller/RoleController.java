package org.myatdental.authoption.roleoptions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.authoption.roleoptions.dto.RoleDTO;
import org.myatdental.authoption.roleoptions.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Integer id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }
    @PostMapping
    public ResponseEntity<RoleDTO> createRole( @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.createRole(roleDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable Integer id,
            @RequestBody RoleDTO roleDTO
    ) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDTO));
    }
    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> assignPermission(
            @PathVariable Integer roleId,
            @PathVariable Long permissionId) {

        roleService.assignPermission(roleId, permissionId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> removePermission(
            @PathVariable Integer roleId,
            @PathVariable Long permissionId) {

        roleService.removePermission(roleId, permissionId);
        return ResponseEntity.noContent().build();
    }


}


