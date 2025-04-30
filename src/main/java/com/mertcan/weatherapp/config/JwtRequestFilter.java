package com.mertcan.weatherapp.config;

import com.mertcan.weatherapp.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        logger.info("JWT Filter Processing URI: {}", requestURI);

        // Health check ve açık endpointler için filtrelemeyi atla
        if (requestURI.equals("/") ||
                requestURI.equals("/health") ||
                requestURI.contains("/h2-console") ||
                requestURI.contains("/api/auth/") ||
                requestURI.contains("/auth/") ||
                requestURI.contains("/api/weather/") ||
                requestURI.contains("/weather/")) {

            logger.info("Skipping JWT validation for path: {}", requestURI);
            chain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader("Authorization");
        logger.debug("Authorization header: {}", requestTokenHeader);

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                logger.info("JWT Token için kullanıcı adı: {}", username);
            } catch (Exception e) {
                logger.warn("JWT Token geçersiz: {}", e.getMessage());
            }
        } else {
            logger.debug("JWT Token 'Bearer ' ile başlamıyor veya bulunamadı");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                logger.debug("Kullanıcı yüklendi: {}", userDetails.getUsername());

                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Kullanıcı doğrulandı: {}", username);
                } else {
                    logger.warn("Token doğrulanamadı");
                }
            } catch (Exception e) {
                logger.error("Kullanıcı yüklenirken hata: {}", e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}