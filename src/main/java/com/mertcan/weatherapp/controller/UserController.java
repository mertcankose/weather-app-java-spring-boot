package com.mertcan.weatherapp.controller;

import com.mertcan.weatherapp.model.User;
import com.mertcan.weatherapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Kullanıcı yönetimi işlemleri")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Tüm kullanıcıları listele",
            description = "Sistemdeki tüm kullanıcıları getirir",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Başarılı",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401",
                    description = "Yetkisiz erişim",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Erişim engellendi",
                    content = @Content)
    })
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}