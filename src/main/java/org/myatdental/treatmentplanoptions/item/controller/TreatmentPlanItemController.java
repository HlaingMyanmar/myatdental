package org.myatdental.treatmentplanoptions.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myatdental.treatmentplanoptions.item.dto.TreatmentPlanItemDTO;
import org.myatdental.treatmentplanoptions.item.service.TreatmentPlanItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/treatment-plans-item")
@RequiredArgsConstructor
public class TreatmentPlanItemController {

    private final TreatmentPlanItemService itemService;


    @PostMapping("/treatment-plans/{planId}/items")
    public ResponseEntity<TreatmentPlanItemDTO> addItemToPlan(
            @PathVariable Integer planId,
            @Valid @RequestBody TreatmentPlanItemDTO dto) {

        TreatmentPlanItemDTO createdItem = itemService.addItemToPlan(planId, dto);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }


    @DeleteMapping("/treatment-plan-items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Integer itemId) {
        itemService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }
}