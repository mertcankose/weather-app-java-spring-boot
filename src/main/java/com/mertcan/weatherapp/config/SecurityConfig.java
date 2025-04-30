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
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORS yapılandırmasını elle tanımla
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setExposedHeaders(java.util.Arrays.asList("Authorization", "Content-Type"));
        corsConfiguration.setMaxAge(3600L);

        http
                // CSRF korumasını devre dışı bırak
                .csrf(csrf -> csrf.disable())

                // CORS yapılandırmasını etkinleştir
                .cors(cors -> cors.configurationSource(request -> corsConfiguration))

                // Tüm isteklere izin ver
                .authorizeHttpRequests(auth -> auth
                        // Root endpoint'i için health check erişimi
                        .requestMatchers("/", "/health").permitAll()
                        // Auth endpointlerine herkes erişebilir
                        .requestMatchers("/api/auth/**", "/auth/**").permitAll()
                        // H2 console'a erişime izin ver
                        .requestMatchers("/h2-console/**").permitAll()
                        // Hava durumu API'si için
                        .requestMatchers("/api/weather/**", "/weather/**").permitAll()
                        // Diğer tüm istekler için izin ver
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