package org.myatdental.paymentmethodoptions.service;
import lombok.RequiredArgsConstructor;
import org.myatdental.paymentmethodoptions.dto.PaymentMethodDTO;
import org.myatdental.paymentmethodoptions.model.PaymentMethods;
import org.myatdental.paymentmethodoptions.respositry.PaymentMethodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Transactional(readOnly = true)
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentMethodDTO getPaymentMethodById(Integer id) {
        PaymentMethods method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found with id: " + id));
        return convertToDTO(method);
    }

    @Transactional
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO dto) {
        // Name ထပ်မနေအောင် စစ်မယ်
        if (paymentMethodRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Payment method already exists: " + dto.getName());
        }

        PaymentMethods method = convertToEntity(dto);
        PaymentMethods savedMethod = paymentMethodRepository.save(method);
        return convertToDTO(savedMethod);
    }

    @Transactional
    public PaymentMethodDTO updatePaymentMethod(Integer id, PaymentMethodDTO dto) {
        PaymentMethods method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found with id: " + id));

        if (!method.getName().equals(dto.getName())
                && paymentMethodRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Payment method already exists: " + dto.getName());
        }

        method.setName(dto.getName());
        method.setDescription(dto.getDescription());
        method.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : method.getIsActive());

        PaymentMethods updatedMethod = paymentMethodRepository.save(method);
        return convertToDTO(updatedMethod);
    }

    @Transactional
    public void deletePaymentMethod(Integer id) {
        if (!paymentMethodRepository.existsById(id)) {
            throw new RuntimeException("Payment method not found with id: " + id);
        }
        paymentMethodRepository.deleteById(id);
    }

    @Transactional
    public PaymentMethodDTO togglePaymentMethodStatus(Integer id) {
        PaymentMethods method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found with id: " + id));

        method.setIsActive(!method.getIsActive());
        return convertToDTO(paymentMethodRepository.save(method));
    }

    private PaymentMethodDTO convertToDTO(PaymentMethods method) {
        PaymentMethodDTO dto = new PaymentMethodDTO();
        dto.setId(method.getId());
        dto.setName(method.getName());
        dto.setDescription(method.getDescription());
        dto.setIsActive(method.getIsActive());
        return dto;
    }

    private PaymentMethods convertToEntity(PaymentMethodDTO dto) {
        PaymentMethods method = new PaymentMethods();
        method.setName(dto.getName());
        method.setDescription(dto.getDescription());
        method.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return method;
    }
}

