package com.example.vmo_project.controller;

import com.example.vmo_project.entity.User;
import com.example.vmo_project.repository.UserRepository;
import com.example.vmo_project.request.LoginRequest;
import com.example.vmo_project.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Tạo đối tượng
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        // Xác thực từ đối tượng
        Authentication authentication = authenticationManager.authenticate(token);

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo token và trả về cho client(front-end)
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String tokenJwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(tokenJwt);
    }
}
