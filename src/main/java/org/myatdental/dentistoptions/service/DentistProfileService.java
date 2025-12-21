package org.myatdental.dentistoptions.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.dentistoptions.dto.DentistDTO;
import org.myatdental.dentistoptions.model.Dentist;
import org.myatdental.dentistoptions.repository.DentistRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DentistProfileService {

    private final DentistRepository dentistRepository;

    @Transactional(readOnly = true)
    public DentistDTO getProfile(String username) {
        Dentist dentist = dentistRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        return convertToDTO(dentist);
    }

    @Transactional
    public DentistDTO updateProfile(String username, DentistDTO dto) {
        Dentist dentist = dentistRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        dentist.setName(dto.getName());
        dentist.setPhone(dto.getPhone());
        dentist.setEmail(dto.getEmail());

        if (dto.getSpecialization() != null) {
            dentist.setSpecialization(dto.getSpecialization());
        }

        // ၄။ Save လုပ်ပြီး DTO အဖြစ် ပြန်ပို့ပါ
        return convertToDTO(dentistRepository.save(dentist));
    }

    private DentistDTO convertToDTO(Dentist dentist) {
        // ... (Mapping logic)
        DentistDTO dto = new DentistDTO();
        BeanUtils.copyProperties(dentist, dto);
        return dto;
    }
}
