package org.myatdental.dentistoptions.controller;

import lombok.RequiredArgsConstructor;
import org.myatdental.dentistoptions.dto.DentistDTO;
import org.myatdental.dentistoptions.service.DentistProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor/profile")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
public class DentistProfileController {

    private  final DentistProfileService dentistProfileService;


    @GetMapping
    @PreAuthorize("hasAuthority('CAN_VIEW_PROFILE')")
    public ResponseEntity<DentistDTO> getMyProfile(Authentication auth) {
        return ResponseEntity.ok(dentistProfileService.getProfile(auth.getName()));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('CAN_EDIT_OWN_PROFILE')")
    public ResponseEntity<DentistDTO> updateMyProfile(Authentication auth, @RequestBody DentistDTO dto) {
        return ResponseEntity.ok(dentistProfileService.updateProfile(auth.getName(), dto));
    }
}
