package com.mertcan.weatherapp.controller;

import com.mertcan.weatherapp.config.JwtTokenUtil;
import com.mertcan.weatherapp.dto.JwtResponse;
import com.mertcan.weatherapp.dto.LoginRequest;
import com.mertcan.weatherapp.dto.RegisterRequest;
import com.mertcan.weatherapp.model.User;
import com.mertcan.weatherapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // Spring anotasyonu, bu sınıfın bir REST controller olduğunu belirtir
@RequestMapping("/api/auth")  // Bu controller'ın temel URL path'i
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;  // Kimlik doğrulama yöneticisi

    @Autowired
    private JwtTokenUtil jwtTokenUtil;  // JWT token işlemleri için yardımcı sınıf

    @Autowired
    private UserService userService;  // Kullanıcı işlemleri için service

    // Kullanıcı girişi endpoint'i
    @PostMapping("/login")  // POST /api/auth/login
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Kullanıcı adı ve şifre doğrulaması
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Doğrulanmış kullanıcı detaylarını alma
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // JWT token oluşturma
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Token'ı yanıt olarak döndürme
        return ResponseEntity.ok(new JwtResponse(token));
    }

    // Kullanıcı kaydı endpoint'i
    @PostMapping("/register")  // POST /api/auth/register
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // Kullanıcı oluşturma işlemini service'e devretme
        User user = userService.createUser(
                registerRequest.getUsername(),
                registerRequest.getPassword()
        );
        return ResponseEntity.ok("Kullanıcı başarıyla kaydedildi");
    }


}