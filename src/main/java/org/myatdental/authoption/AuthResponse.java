package org.myatdental.authoption;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class AuthResponse { private String token; private String username; }
