package org.myatdental.treatmentplanoptions.plan.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentoptions.repository.TreatmentsRepository;
import org.myatdental.treatmentplanoptions.plan.dto.TreatmentPlanDTO;
import org.myatdental.treatmentplanoptions.item.dto.TreatmentPlanItemDTO;
import org.myatdental.treatmentplanoptions.plan.model.TreatmentPlan;
import org.myatdental.treatmentplanoptions.item.model.TreatmentPlanItem;
import org.myatdental.treatmentplanoptions.plan.repository.TreatmentPlanRepository;
import org.myatdental.treatmentoptions.model.Treatments;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentPlanService {

    private final TreatmentPlanRepository planRepository;
    private final TreatmentsRepository treatmentRepository;


    @Transactional
    public TreatmentPlanDTO createPlan(TreatmentPlanDTO dto) {
        if (planRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Treatment Plan code already exists: " + dto.getCode());
        }

        TreatmentPlan plan = new TreatmentPlan();
        plan.setCode(dto.getCode());
        plan.setName(dto.getName());
        plan.setIsTemplate(dto.getIsTemplate() != null ? dto.getIsTemplate() : false);
        plan.setInstallmentsAllowed(dto.getInstallmentsAllowed() != null ? dto.getInstallmentsAllowed() : false);

        BigDecimal total = BigDecimal.ZERO;

        // DTO ထဲမှာပါလာတဲ့ Item list ကို Entity ပြောင်းပြီး Plan ထဲထည့်ခြင်း
        if (dto.getItems() != null) {
            for (TreatmentPlanItemDTO itemDto : dto.getItems()) {
                Treatments treatment = treatmentRepository.findById(itemDto.getTreatmentId())
                        .orElseThrow(() -> new RuntimeException("Treatment not found"));

                TreatmentPlanItem item = new TreatmentPlanItem();
                item.setPlan(plan);
                item.setTreatment(treatment);
                item.setEstimatedPrice(itemDto.getEstimatedPrice());

                plan.getItems().add(item);
                total = total.add(itemDto.getEstimatedPrice());
            }
        }

        plan.setTotalCost(total); // စုစုပေါင်းကုန်ကျစရိတ်ကို တစ်ခါတည်း တွက်ချက်သည်
        return convertToDTO(planRepository.save(plan));
    }


    @Transactional(readOnly = true)
    public TreatmentPlanDTO getPlanById(Integer id) {
        TreatmentPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment Plan not found with id: " + id));
        return convertToDTO(plan);
    }


    @Transactional(readOnly = true)
    public List<TreatmentPlanDTO> getTemplates() {
        return planRepository.findByIsTemplateTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePlan(Integer id) {
        if (!planRepository.existsById(id)) {
            throw new RuntimeException("Plan not found");
        }
        planRepository.deleteById(id);
    }
    private TreatmentPlanDTO convertToDTO(TreatmentPlan plan) {
        TreatmentPlanDTO dto = new TreatmentPlanDTO();
        dto.setPlanId(plan.getPlanId());
        dto.setCode(plan.getCode());
        dto.setName(plan.getName());
        dto.setIsTemplate(plan.getIsTemplate());
        dto.setTotalCost(plan.getTotalCost());
        dto.setInstallmentsAllowed(plan.getInstallmentsAllowed());
        dto.setIsActive(plan.getIsActive());

        if (plan.getItems() != null) {
            dto.setItems(plan.getItems().stream().map(item -> {
                TreatmentPlanItemDTO iDto = new TreatmentPlanItemDTO();
                iDto.setItemId(item.getItemId());
                iDto.setTreatmentId(item.getTreatment().getTreatmentId());
                iDto.setTreatmentName(item.getTreatment().getCode()); // Code သို့မဟုတ် Name ပြနိုင်သည်
                iDto.setEstimatedPrice(item.getEstimatedPrice());
                return iDto;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
    @Transactional
    public TreatmentPlanDTO updatePlan(Integer id, TreatmentPlanDTO dto) {
        TreatmentPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment Plan not found with id: " + id));


        if (!plan.getCode().equals(dto.getCode()) && planRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Plan code already exists: " + dto.getCode());
        }


        plan.setName(dto.getName());
        plan.setCode(dto.getCode());
        plan.setIsTemplate(dto.getIsTemplate());
        plan.setInstallmentsAllowed(dto.getInstallmentsAllowed());
        plan.setIsActive(dto.getIsActive());

        plan.getItems().clear();
        BigDecimal total = BigDecimal.ZERO;

        if (dto.getItems() != null) {
            for (var itemDto : dto.getItems()) {
                Treatments treatment = treatmentRepository.findById(itemDto.getTreatmentId())
                        .orElseThrow(() -> new RuntimeException("Treatment not found"));

                TreatmentPlanItem item = new TreatmentPlanItem();
                item.setPlan(plan);
                item.setTreatment(treatment);
                item.setEstimatedPrice(itemDto.getEstimatedPrice());

                plan.getItems().add(item);
                total = total.add(itemDto.getEstimatedPrice());
            }
        }
        plan.setTotalCost(total);
        return convertToDTO(planRepository.save(plan));
    }
}