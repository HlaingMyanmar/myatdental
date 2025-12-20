package org.myatdental.treatmentplanoptions.item.service;

import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentoptions.repository.TreatmentsRepository;
import org.myatdental.treatmentplanoptions.item.dto.TreatmentPlanItemDTO;
import org.myatdental.treatmentplanoptions.plan.model.TreatmentPlan;
import org.myatdental.treatmentplanoptions.item.model.TreatmentPlanItem;
import org.myatdental.treatmentplanoptions.item.repository.TreatmentPlanItemRepository;
import org.myatdental.treatmentoptions.model.Treatments;
import org.myatdental.treatmentplanoptions.plan.repository.TreatmentPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentPlanItemService {

    private final TreatmentPlanItemRepository itemRepository;
    private final TreatmentPlanRepository planRepository;
    private final TreatmentsRepository treatmentRepository;

    @Transactional
    public TreatmentPlanItemDTO addItemToPlan(Integer planId, TreatmentPlanItemDTO dto) {
        TreatmentPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Treatments treatment = treatmentRepository.findById(dto.getTreatmentId())
                .orElseThrow(() -> new RuntimeException("Treatment not found"));

        TreatmentPlanItem item = new TreatmentPlanItem();
        item.setPlan(plan);
        item.setTreatment(treatment);
        item.setEstimatedPrice(dto.getEstimatedPrice());

        itemRepository.save(item);

        recalculateTotalCost(planId); // Plan ၏ Total Cost ကို ပြန်တွက်ရန်

        return convertToDTO(item);
    }

    @Transactional
    public void removeItem(Integer itemId) {
        TreatmentPlanItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Integer planId = item.getPlan().getPlanId();
        itemRepository.delete(item);

        recalculateTotalCost(planId); // Plan ၏ Total Cost ကို ပြန်တွက်ရန်
    }

    // --- စုစုပေါင်းကုန်ကျစရိတ်ကို ပြန်လည်တွက်ချက်ပေးသော Helper Method ---
    private void recalculateTotalCost(Integer planId) {
        TreatmentPlan plan = planRepository.findById(planId).orElseThrow();
        List<TreatmentPlanItem> items = itemRepository.findByPlanPlanId(planId);

        BigDecimal total = items.stream()
                .map(TreatmentPlanItem::getEstimatedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        plan.setTotalCost(total);
        planRepository.save(plan);
    }

    private TreatmentPlanItemDTO convertToDTO(TreatmentPlanItem item) {
        TreatmentPlanItemDTO dto = new TreatmentPlanItemDTO();
        dto.setItemId(item.getItemId());
        dto.setTreatmentId(item.getTreatment().getTreatmentId());
        dto.setTreatmentName(item.getTreatment().getCode());
        dto.setEstimatedPrice(item.getEstimatedPrice());
        return dto;
    }
}