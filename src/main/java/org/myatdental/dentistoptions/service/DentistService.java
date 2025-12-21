package org.myatdental.dentistoptions.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.authoption.useroptions.model.User; // User Model ကို Import လုပ်ပါ
import org.myatdental.authoption.useroptions.repository.UserRepository;
import org.myatdental.dentistoptions.dto.DentistDTO;
import org.myatdental.dentistoptions.model.Dentist;
import org.myatdental.dentistoptions.repository.DentistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DentistService {

    private final DentistRepository dentistRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<DentistDTO> getAllDentists() {
        return dentistRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DentistDTO getDentistById(Long id) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentist not found with id: " + id));
        return convertToDTO(dentist);
    }

    @Transactional
    public DentistDTO createDentist(DentistDTO dto) {
        if (dentistRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Dentist code already exists: " + dto.getCode());
        }
        if (dto.getEmail() != null && dentistRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists: " + dto.getEmail());
        }

        Dentist dentist = convertToEntity(dto);

        // [သစ်] User Account နှင့် ချိတ်ဆက်ခြင်း
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
            dentist.setUser(user);
        }

        Dentist savedDentist = dentistRepository.save(dentist);
        return convertToDTO(savedDentist);
    }
    // DentistService ထဲတွင် ထည့်ရန်
    public DentistDTO getDentistByUsername(String username) {
        // User ကနေတစ်ဆင့် Dentist ကို ရှာတဲ့ logic
        return dentistRepository.findByUserUsername(username)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user: " + username));
    }

    @Transactional
    public DentistDTO updateDentist(Long id, DentistDTO dto) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentist not found with id: " + id));

        if (!dentist.getCode().equals(dto.getCode()) && dentistRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Dentist code already exists: " + dto.getCode());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(dentist.getEmail())
                && dentistRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists: " + dto.getEmail());
        }

        dentist.setName(dto.getName());
        dentist.setCode(dto.getCode());
        dentist.setSpecialization(dto.getSpecialization());
        dentist.setPhone(dto.getPhone());
        dentist.setEmail(dto.getEmail());
        dentist.setJoinDate(dto.getJoinDate());
        dentist.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : dentist.getIsActive());

        // [သစ်] User Account ပြောင်းလဲခြင်း သို့မဟုတ် အသစ်ချိတ်ဆက်ခြင်း
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            dentist.setUser(user);
        }

        Dentist updatedDentist = dentistRepository.save(dentist);
        return convertToDTO(updatedDentist);
    }

    @Transactional
    public void deleteDentist(Long id) {
        if (!dentistRepository.existsById(id)) {
            throw new RuntimeException("Dentist not found with id: " + id);
        }
        dentistRepository.deleteById(id);
    }
    @Transactional
    public DentistDTO unlinkUserAccount(Long id) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentist not found"));

        dentist.setUser(null);
        return convertToDTO(dentistRepository.save(dentist));
    }

    @Transactional
    public DentistDTO toggleDentistStatus(Long id) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentist not found with id: " + id));
        dentist.setIsActive(!dentist.getIsActive());
        return convertToDTO(dentistRepository.save(dentist));
    }

    private DentistDTO convertToDTO(Dentist dentist) {
        DentistDTO dto = new DentistDTO();
        dto.setId(dentist.getId());
        dto.setCode(dentist.getCode());
        dto.setName(dentist.getName());
        dto.setSpecialization(dentist.getSpecialization());
        dto.setPhone(dentist.getPhone());
        dto.setEmail(dentist.getEmail());
        dto.setIsActive(dentist.getIsActive());
        dto.setJoinDate(dentist.getJoinDate());

        // [သစ်] DTO ထဲသို့ userId ထည့်သွင်းပေးခြင်း
        if (dentist.getUser() != null) {
            dto.setUserId(dentist.getUser().getId());
        }
        return dto;
    }

    private Dentist convertToEntity(DentistDTO dto) {
        Dentist dentist = new Dentist();
        dentist.setCode(dto.getCode());
        dentist.setName(dto.getName());
        dentist.setSpecialization(dto.getSpecialization());
        dentist.setPhone(dto.getPhone());
        dentist.setEmail(dto.getEmail());
        dentist.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        dentist.setJoinDate(dto.getJoinDate());
        // User ကို createDentist/updateDentist method ထဲတွင် ရှာပြီး ထည့်ပေးမည်
        return dentist;
    }
    @Transactional
    public DentistDTO updateProfileByUsername(String username, DentistDTO dto) {

            Dentist dentist = dentistRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        dentist.setName(dto.getName());
        dentist.setSpecialization(dto.getSpecialization());
        dentist.setPhone(dto.getPhone());
        dentist.setEmail(dto.getEmail());

        return convertToDTO(dentistRepository.save(dentist));
    }
}