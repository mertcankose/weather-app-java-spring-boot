package com.mertcan.weatherapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //@Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF korumasını devre dışı bırak
                .csrf(csrf -> csrf.disable())

                // Hava durumu API'si için JWT doğrulaması gerekiyor
                .authorizeHttpRequests(auth -> auth
                        // Auth endpointlerine herkes erişebilir
                        .requestMatchers("/api/auth/**").permitAll()
                        // H2 console'a erişime izin ver (geliştirme için)
                        .requestMatchers("/h2-console/**").permitAll()
                        // health
                        .requestMatchers("/health", "/").permitAll()
                        // Hava durumu API'si JWT doğrulaması gerektirir
                        .requestMatchers("/api/weather/**").authenticated()
                        // Diğer tüm istekler için izin ver (bunu ihtiyaca göre güncelleyebilirsiniz)
                        .anyRequest().permitAll()
                )

                // Oturum yönetimini stateless yap
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // H2 konsolu için frame options izni
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                );

        // JWT filtresini UsernamePasswordAuthenticationFilter'dan önce ekle
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}