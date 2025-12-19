package org.myatdental.authoption;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
class AuthRequest {
    private String identifier;
    private String password;
}