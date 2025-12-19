package org.myatdental.authoption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class AuthResponse {
    private String accessToken;
    private String username;
}