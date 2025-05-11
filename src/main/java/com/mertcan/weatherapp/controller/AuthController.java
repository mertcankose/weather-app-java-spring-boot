package com.mertcan.weatherapp.controller;

import com.mertcan.weatherapp.config.JwtTokenUtil;
import com.mertcan.weatherapp.dto.JwtResponse;
import com.mertcan.weatherapp.dto.LoginRequest;
import com.mertcan.weatherapp.dto.RegisterRequest;
import com.mertcan.weatherapp.model.User;
import com.mertcan.weatherapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Kimlik doğrulama işlemleri")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Kullanıcı girişi",
            description = "Kullanıcı adı ve şifre ile giriş yaparak JWT token alınır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Başarılı giriş",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "Geçersiz kullanıcı adı veya şifre",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Geçersiz istek",
                    content = @Content)
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Kullanıcı kaydı",
            description = "Yeni kullanıcı hesabı oluşturur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Başarılı kayıt",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "Kullanıcı zaten mevcut",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Geçersiz istek",
                    content = @Content)
    })
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        User user = userService.createUser(
                registerRequest.getUsername(),
                registerRequest.getPassword()
        );
        return ResponseEntity.ok("Kullanıcı başarıyla kaydedildi");
    }
}