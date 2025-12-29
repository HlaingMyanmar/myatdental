package org.myatdental.vitaloption.type.service;

import lombok.RequiredArgsConstructor;

import org.myatdental.vitaloption.type.dto.VitalTypeDTO;
import org.myatdental.vitaloption.type.model.VitalType;
import org.myatdental.vitaloption.type.repository.VitalTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VitalTypeService {

    private final VitalTypeRepository vitalTypeRepository;

    @Transactional
    public VitalTypeDTO createVitalType(VitalTypeDTO dto) {
        if (vitalTypeRepository.findByVitalName(dto.getVitalName()).isPresent()) {
            throw new RuntimeException("Vital Type with name '" + dto.getVitalName() + "' already exists.");
        }
        VitalType vitalType = new VitalType();
        vitalType.setVitalName(dto.getVitalName());
        vitalType.setUnit(dto.getUnit());
        vitalType.setDescription(dto.getDescription());
        vitalType.setIsActive(true); // အသစ်ဖန်တီးရင် active ဖြစ်မယ်။
        return convertToDTO(vitalTypeRepository.save(vitalType));
    }

    @Transactional(readOnly = true)
    public List<VitalTypeDTO> getAllVitalTypes() {
        return vitalTypeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VitalTypeDTO getVitalTypeById(Integer id) {
        return vitalTypeRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Vital Type not found with ID: " + id));
    }

    @Transactional
    public VitalTypeDTO updateVitalType(Integer id, VitalTypeDTO dto) {
        VitalType vitalType = vitalTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vital Type not found with ID: " + id));

        // အမည်ပြောင်းလဲလျှင် unique ဖြစ်မဖြစ် စစ်ဆေး
        if (!vitalType.getVitalName().equals(dto.getVitalName()) &&
                vitalTypeRepository.findByVitalName(dto.getVitalName()).isPresent()) {
            throw new RuntimeException("Vital Type with name '" + dto.getVitalName() + "' already exists.");
        }

        vitalType.setVitalName(dto.getVitalName());
        vitalType.setUnit(dto.getUnit());
        vitalType.setDescription(dto.getDescription());
        vitalType.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : vitalType.getIsActive());
        return convertToDTO(vitalTypeRepository.save(vitalType));
    }

    @Transactional
    public void deleteVitalType(Integer id) {
        if (!vitalTypeRepository.existsById(id)) {
            throw new RuntimeException("Vital Type not found with ID: " + id);
        }
        vitalTypeRepository.deleteById(id);
    }

    @Transactional
    public VitalTypeDTO toggleVitalTypeStatus(Integer id) {
        VitalType vitalType = vitalTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vital Type not found with ID: " + id));
        vitalType.setIsActive(!vitalType.getIsActive());
        return convertToDTO(vitalTypeRepository.save(vitalType));
    }

    private VitalTypeDTO convertToDTO(VitalType vitalType) {
        VitalTypeDTO dto = new VitalTypeDTO();
        dto.setTypeId(vitalType.getTypeId());
        dto.setVitalName(vitalType.getVitalName());
        dto.setUnit(vitalType.getUnit());
        dto.setDescription(vitalType.getDescription());
        dto.setIsActive(vitalType.getIsActive());
        return dto;
    }

    // DTO to Entity ပြောင်းလဲရန် လိုအပ်ပါက ဤနေရာတွင် ရေးပါ။
    // private VitalType convertToEntity(VitalTypeDTO dto) { /* ... */ }
}