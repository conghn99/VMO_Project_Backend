package com.example.vmo_project.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class CustomFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Lấy token từ header
        // Authorization : Bearer jdkalcnmmkksks
        String authorizationToken = request.getHeader("Authorization");
        if(authorizationToken == null || !authorizationToken.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationToken.substring(7);
        log.info("Token : {}", token);

        // Lấy thông tin từ trong token
        Claims claims = jwtTokenUtil.getClaimsFromToken(token);

        // Lấy username từ trong token
        String username = claims.getSubject();
        log.info("Username : {}", username);

        // Kiểm tra username
        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy thông tin user
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Tạo đối tượng xác thực
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Xác thực thành công, lưu object Authentication vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
