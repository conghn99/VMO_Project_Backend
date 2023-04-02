package com.example.vmo_project.service;

import com.example.vmo_project.request.LoginRequest;
import com.example.vmo_project.response.LoginResponse;
import com.example.vmo_project.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public LoginResponse authenticateUser(LoginRequest request) {
        // Tạo đối tượng
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(token);

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo token và trả về cho client
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String tokenJwt = jwtTokenUtil.generateToken(userDetails);

        return new LoginResponse(userDetails, tokenJwt, true);
    }
}
