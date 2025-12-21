package org.myatdental.customSecurityService;

import lombok.RequiredArgsConstructor;
import org.myatdental.Jwt.CustomUserDetails;
import org.myatdental.dentistoptions.repository.DentistRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
@Component("ss")
@RequiredArgsConstructor
public class SecurityService {

    private final DentistRepository dentistRepository;


    public boolean isSuperAdmin() {
        return getAuth().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("ROLE_ADMINISTRATOR"));
    }

    public boolean isDentistWithPermission(String permissionName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        boolean hasPermission = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(permissionName));
        if (!hasPermission) return false;

        return dentistRepository.findByUserUsername(auth.getName()).isPresent();
    }
    public boolean canUpdateDentist(Long targetId, String permissionName) {
        // ၁။ Admin ဆိုရင် အကုန်ပေးလုပ်မယ်
        if (isSuperAdmin()) return true;

        // ၂။ Admin မဟုတ်ရင် - Permission ရှိရမယ်၊ ပြီးတော့ ပြင်မယ့် ID ဟာ ကိုယ့် ID ဖြစ်ရမယ်
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        boolean hasPermission = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(permissionName));

        if (!hasPermission) return false;

        // Login ဝင်ထားတဲ့ ဆရာဝန်ရဲ့ ID နဲ့ URL က targetId တူမတူ စစ်ခြင်း
        return dentistRepository.findByUserUsername(auth.getName())
                .map(dentist -> dentist.getId().equals(targetId))
                .orElse(false);
    }



    public boolean canEditProfile(Long targetUserId) {
        if (isSuperAdmin()) return true;
        return getCurrentUserId().equals(targetUserId);
    }

    public boolean hasPermission(String permissionName) {
        return getAuth().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(permissionName));
    }

    public boolean canAccessDentist(Long targetDentistId) {
        if (isSuperAdmin()) return true;

        String username = getAuth().getName();
        return dentistRepository.findByUserUsername(username)
                .map(d -> d.getId().equals(targetDentistId))
                .orElse(false);
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private Long getCurrentUserId() {

        return ((CustomUserDetails) getAuth().getPrincipal()).getId();
    }
}