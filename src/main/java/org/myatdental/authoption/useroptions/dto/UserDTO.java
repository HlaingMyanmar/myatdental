package org.myatdental.authoption.useroptions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password_hash;
    private String authProvider;
    private Boolean isActive;
    private Set<String> roles;
    private Long dentistId;

}