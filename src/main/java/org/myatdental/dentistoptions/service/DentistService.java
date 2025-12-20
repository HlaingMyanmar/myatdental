package org.myatdental.dentistoptions.service;

import lombok.RequiredArgsConstructor;
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
        // Code ထပ်မနေအောင် စစ်မယ်
        if (dentistRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Dentist code already exists: " + dto.getCode());
        }
        // Email ပါလာရင် ထပ်မနေအောင် စစ်မယ်
        if (dto.getEmail() != null && dentistRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists: " + dto.getEmail());
        }

        Dentist dentist = convertToEntity(dto);
        Dentist savedDentist = dentistRepository.save(dentist);
        return convertToDTO(savedDentist);
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
        return dentist;
    }
}