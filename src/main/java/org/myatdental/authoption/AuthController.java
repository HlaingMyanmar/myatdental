package org.myatdental.authoption;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.myatdental.Jwt.CustomUserDetailsService;
import org.myatdental.Jwt.JwtService;
import org.myatdental.authoption.useroptions.dto.UserDTO;
import org.myatdental.authoption.useroptions.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO dto) {

        return ResponseEntity.ok(userService.createUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword())
        );

        var userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();

        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        userDetails.getAuthorities().forEach(authority -> {
            String auth = authority.getAuthority();
            if (auth.startsWith("ROLE_")) {
                roles.add(auth); // ROLE_ADMIN, ROLE_DOCTOR စသည်
            } else {
                permissions.add(auth); // CAN_EDIT_PROFILE, VIEW_PATIENTS စသည်
            }
        });

        String accessToken = jwtService.generateToken(userDetails);


        String refreshToken = jwtService.generateToken(userDetails);


        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new AuthResponse(
                accessToken,
                userDetails.getUsername(),
                roles,
                permissions
        ));
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();


        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            String newAccessToken = jwtService.generateToken(userDetails);
          //  return ResponseEntity.ok(new AuthResponse(newAccessToken, username));
            Set<String> roles = new HashSet<>();
            Set<String> permissions = new HashSet<>();

            userDetails.getAuthorities().forEach(authority -> {
                String auth = authority.getAuthority();
                if (auth.startsWith("ROLE_")) {
                    roles.add(auth);
                } else {
                    permissions.add(auth);
                }
            });
            return ResponseEntity.ok(new AuthResponse(
                    newAccessToken,
                    username,
                    roles,
                    permissions
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
