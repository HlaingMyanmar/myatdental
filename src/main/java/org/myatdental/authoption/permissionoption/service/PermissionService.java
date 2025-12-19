package org.myatdental.authoption.permissionoption.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.authoption.permissionoption.dto.PermissionDTO;
import org.myatdental.authoption.permissionoption.model.Permission;
import org.myatdental.authoption.permissionoption.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PermissionDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        return convertToDTO(permission);
    }

    @Transactional
    public PermissionDTO createPermission(PermissionDTO dto) {
        permissionRepository.findByName(dto.getName())
                .ifPresent(p -> { throw new RuntimeException("Permission already exists: " + dto.getName()); });

        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        return convertToDTO(permissionRepository.save(permission));
    }

    @Transactional
    public PermissionDTO updatePermission(Long id, PermissionDTO dto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));

        if (!permission.getName().equals(dto.getName())) {
            permissionRepository.findByName(dto.getName())
                    .ifPresent(p -> { throw new RuntimeException("Permission name already exists: " + dto.getName()); });
        }

        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());

        return convertToDTO(permissionRepository.save(permission));
    }

    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        permissionRepository.delete(permission);
    }

    private PermissionDTO convertToDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        return dto;
    }
}
