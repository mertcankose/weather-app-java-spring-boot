package com.mertcan.weatherapp.config;

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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    // Constructor injection
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS yapılandırmasını ekle
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF korumasını devre dışı bırak
                .csrf(csrf -> csrf.disable())

                // Hava durumu API'si için JWT doğrulaması gerekiyor
                .authorizeHttpRequests(auth -> auth
                        // OPTIONS requeste izin ver (CORS preflight için)
                        .requestMatchers("OPTIONS", "/**").permitAll()
                        // Auth endpointlerine herkes erişebilir
                        .requestMatchers("/api/auth/**").permitAll()
                        // H2 console'a erişime izin ver (geliştirme için)
                        .requestMatchers("/h2-console/**").permitAll()
                        // health
                        .requestMatchers("/health", "/", "/api/test/**").permitAll()
                        // Hava durumu API'si JWT doğrulaması gerektirir
                        .requestMatchers("/api/weather/**").authenticated()
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // İzin verilen origin'leri belirt
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));  // Tüm origin'lere izin ver (development için)
        // Veya belirli origin'leri kullanmak için:
        // configuration.setAllowedOrigins(Arrays.asList("https://weather-cesium.vercel.app", "http://localhost:3000"));

        // İzin verilen HTTP metotları
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // İzin verilen header'lar
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Client'ın hangi header'ları görebileceğini belirt
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        // Kimlik bilgilerine izin ver (cookies, auth headers vb.)
        configuration.setAllowCredentials(true);

        // Preflight request'lerin önbelleğe alınma süresi
        configuration.setMaxAge(3600L);

        // Hangi URL path'leri için bu yapılandırmanın geçerli olacağını belirt
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
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