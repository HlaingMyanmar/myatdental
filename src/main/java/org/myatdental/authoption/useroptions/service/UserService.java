package org.myatdental.authoption.useroptions.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.authoption.roleoptions.model.Role;
import org.myatdental.authoption.roleoptions.repository.RoleRepository;
import org.myatdental.authoption.useroptions.dto.UserDTO;
import org.myatdental.authoption.useroptions.model.User;
import org.myatdental.authoption.useroptions.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists: " + dto.getEmail());
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists: " + dto.getUsername());
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword_hash()));
        user.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        user.setAuthProvider(dto.getAuthProvider() != null ? dto.getAuthProvider() : "LOCAL");

        if (dto.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            dto.getRoles().forEach(r -> {
                Role role = roleRepository.findByName(r)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + r));
                roles.add(role);
            });
            user.setRoles(roles);
        }

        return convertToDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists: " + dto.getEmail());
        }
        if (!user.getUsername().equals(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists: " + dto.getUsername());
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        if(dto.getPassword_hash()!=null){
            user.setPassword(passwordEncoder.encode(dto.getPassword_hash()));
        }
        user.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : user.getIsActive());
        user.setAuthProvider(dto.getAuthProvider() != null ? dto.getAuthProvider() : user.getAuthProvider());

        if (dto.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            dto.getRoles().forEach(r -> {
                Role role = roleRepository.findByName(r)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + r));
                roles.add(role);
            });
            user.getRoles().clear();
            user.getRoles().addAll(roles);
        }

        return convertToDTO(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.getRoles().clear(); // remove associations
        userRepository.delete(user);
    }

    @Transactional
    public UserDTO toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setIsActive(!user.getIsActive());

        return convertToDTO(userRepository.save(user));
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAuthProvider(user.getAuthProvider());
        dto.setIsActive(user.getIsActive());


        if (user.getRoles() != null) {
            Set<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            dto.setRoles(roles);
        } else {
            dto.setRoles(new HashSet<>());
        }

        return dto;
    }
}
