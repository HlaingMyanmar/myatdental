package org.myatdental.Jwt;


import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private final Long id; // Database ID ကို သိမ်းရန်

    public CustomUserDetails(Long id, String username, String password, boolean enabled,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
