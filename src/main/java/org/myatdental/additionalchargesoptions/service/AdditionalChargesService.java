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
            throw new RuntimeException("Charge name '" + dto.getName() + "' already exists.");
        }

        AdditionalCharges charge = convertToEntity(dto);

        charge.setChargeId(null);

        AdditionalCharges savedCharge = additionalChargesRepository.save(charge);
        return convertToDTO(savedCharge);
    }

    @Transactional
    public AdditionalChargesDTO updateCharge(Integer id, AdditionalChargesDTO dto) {
        AdditionalCharges charge = additionalChargesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charge not found with id: " + id));


        if (!charge.getName().equalsIgnoreCase(dto.getName()) &&
                additionalChargesRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Charge name '" + dto.getName() + "' already exists.");
        }

        charge.setName(dto.getName());
        charge.setDescription(dto.getDescription());
        charge.setDefaultPrice(dto.getDefaultPrice() != null ? dto.getDefaultPrice() : charge.getDefaultPrice());
        charge.setIs_active(dto.getIsActive() != null ? dto.getIsActive() : charge.getIs_active());

        return convertToDTO(additionalChargesRepository.save(charge));
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

    // --- Helper Methods ---

    private AdditionalChargesDTO convertToDTO(AdditionalCharges charge) {
        AdditionalChargesDTO dto = new AdditionalChargesDTO();
        dto.setChargeId(charge.getChargeId());
        dto.setName(charge.getName());
        dto.setDescription(charge.getDescription());
        dto.setDefaultPrice(charge.getDefaultPrice());
        dto.setIsActive(charge.getIs_active());
        return dto;
    }

    private AdditionalCharges convertToEntity(AdditionalChargesDTO dto) {
        AdditionalCharges charge = new AdditionalCharges();
        charge.setName(dto.getName());
        charge.setDescription(dto.getDescription());
        // Default values များ သတ်မှတ်ခြင်း
        charge.setDefaultPrice(dto.getDefaultPrice() != null ? dto.getDefaultPrice() : BigDecimal.ZERO);
        charge.setIs_active(dto.getIsActive() != null ? dto.getIsActive() : true);
        return charge;
    }
}