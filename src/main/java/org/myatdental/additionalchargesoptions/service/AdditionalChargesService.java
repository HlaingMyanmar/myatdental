package org.myatdental.additionalchargesoptions.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.additionalchargesoptions.dto.AdditionalChargesDTO;
import org.myatdental.additionalchargesoptions.model.AdditionalCharges;
import org.myatdental.additionalchargesoptions.repository.AdditionalChargesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdditionalChargesService {

    private final AdditionalChargesRepository additionalChargesRepository;

    @Transactional(readOnly = true)
    public List<AdditionalChargesDTO> getAllCharges() {
        return additionalChargesRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdditionalChargesDTO getChargeById(Integer id) {
        AdditionalCharges charge = additionalChargesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charge not found with id: " + id));
        return convertToDTO(charge);
    }

    @Transactional
    public AdditionalChargesDTO createCharge(AdditionalChargesDTO dto) {
        if (additionalChargesRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Charge name already exists: " + dto.getName());
        }

        AdditionalCharges charge = convertToEntity(dto);
        AdditionalCharges savedCharge = additionalChargesRepository.save(charge);
        return convertToDTO(savedCharge);
    }

    @Transactional
    public AdditionalChargesDTO updateCharge(Integer id, AdditionalChargesDTO dto) {
        AdditionalCharges charge = additionalChargesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charge not found with id: " + id));

        if (!charge.getName().equals(dto.getName()) && additionalChargesRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Charge name already exists: " + dto.getName());
        }

        charge.setName(dto.getName());
        charge.setDescription(dto.getDescription());
        charge.setDefault_price(dto.getDefault_price() != null ? dto.getDefault_price() : charge.getDefault_price());
        charge.setIs_active(dto.getIs_active() != null ? dto.getIs_active() : charge.getIs_active());

        AdditionalCharges updatedCharge = additionalChargesRepository.save(charge);
        return convertToDTO(updatedCharge);
    }

    @Transactional
    public void deleteCharge(Integer id) {
        if (!additionalChargesRepository.existsById(id)) {
            throw new RuntimeException("Charge not found with id: " + id);
        }
        additionalChargesRepository.deleteById(id);
    }

    @Transactional
    public AdditionalChargesDTO toggleChargeStatus(Integer id) {
        AdditionalCharges charge = additionalChargesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charge not found with id: " + id));

        charge.setIs_active(!charge.getIs_active());
        return convertToDTO(additionalChargesRepository.save(charge));
    }

    private AdditionalChargesDTO convertToDTO(AdditionalCharges charge) {
        AdditionalChargesDTO dto = new AdditionalChargesDTO();
        dto.setCharge_id(charge.getCharge_id());
        dto.setName(charge.getName());
        dto.setDescription(charge.getDescription());
        dto.setDefault_price(charge.getDefault_price());
        dto.setIs_active(charge.getIs_active());
        return dto;
    }

    private AdditionalCharges convertToEntity(AdditionalChargesDTO dto) {
        AdditionalCharges charge = new AdditionalCharges();
        charge.setName(dto.getName());
        charge.setDescription(dto.getDescription());
        charge.setDefault_price(dto.getDefault_price() != null ? dto.getDefault_price() : BigDecimal.ZERO);
        charge.setIs_active(dto.getIs_active() != null ? dto.getIs_active() : true);
        return charge;
    }
}
