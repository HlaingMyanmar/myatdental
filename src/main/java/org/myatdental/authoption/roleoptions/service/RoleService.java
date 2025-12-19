package org.myatdental.authoption.roleoptions.service;


import lombok.RequiredArgsConstructor;
import org.myatdental.authoption.permissionoption.dto.PermissionDTO;
import org.myatdental.authoption.permissionoption.model.Permission;
import org.myatdental.authoption.permissionoption.repository.PermissionRepository;
import org.myatdental.authoption.roleoptions.dto.RoleDTO;
import org.myatdental.authoption.roleoptions.model.Role;
import org.myatdental.authoption.roleoptions.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {

        roleRepository.findByName(roleDTO.getName())
                .ifPresent(role -> {
                    throw new RuntimeException("Role already exists with name: " + roleDTO.getName());
                });
        Role role = convertToEntity(roleDTO);
        if (roleDTO.getPermissions() != null) {
            Set<Permission> permissions = roleDTO.getPermissions().stream()
                    .map(p -> permissionRepository.findById(p.getId())
                            .orElseThrow(() ->
                                    new RuntimeException("Permission not found: " + p.getId())
                            ))
                    .collect(Collectors.toSet());

            role.setPermissions(permissions);
        }
        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }

    @Transactional
    public RoleDTO updateRole(Integer roleId, RoleDTO roleDTO) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with id: " + roleId)
                );
        if (!role.getName().equals(roleDTO.getName())) {
            roleRepository.findByName(roleDTO.getName())
                    .ifPresent(r -> {
                        throw new RuntimeException("Role name already exists: " + roleDTO.getName());
                    });
        }
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        if (roleDTO.getPermissions() != null) {
            Set<Permission> permissions = roleDTO.getPermissions().stream()
                    .map(p -> permissionRepository.findById(p.getId())
                            .orElseThrow(() ->
                                    new RuntimeException("Permission not found: " + p.getId())
                            ))
                    .collect(Collectors.toSet());

            role.getPermissions().clear();
            role.getPermissions().addAll(permissions);
        }
        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }
    @Transactional
    public void deleteRole(Integer roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with id: " + roleId)
                );
        role.getPermissions().clear();
        roleRepository.delete(role);
    }
    public RoleDTO getRoleById(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with id: " + id)
                );
        return convertToDTO(role);
    }



    private RoleDTO convertToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());

        // Role ထဲက Permission တွေကို DTO ထဲ ထည့်ပေးတဲ့ logic ပါမှ data ပေါ်မှာပါ
        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            dto.setPermissions(role.getPermissions().stream()
                    .map(p -> {
                        PermissionDTO pdto = new PermissionDTO();
                        pdto.setId(p.getId());
                        pdto.setName(p.getName());
                        pdto.setDescription(p.getDescription());
                        return pdto;
                    }).collect(Collectors.toList()));
        } else {
            dto.setPermissions(new ArrayList<>()); // Empty list ပေးထားပါ
        }
        return dto;
    }

    private Role convertToEntity(RoleDTO roleDTO) {
        return Role.builder()
                .id(roleDTO.getId())
                .name(roleDTO.getName())
                .description(roleDTO.getDescription())
                .build();
    }

    @Transactional
    public void assignPermission(Integer roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));

        role.getPermissions().add(permission);
        roleRepository.save(role);
    }
    @Transactional
    public void removePermission(Integer roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));


        role.getPermissions().remove(permission);
        roleRepository.save(role);
    }
}
