package com.mertcan.weatherapp.service;

import com.mertcan.weatherapp.model.User;
import com.mertcan.weatherapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service  // Spring anotasyonu, bu sınıfın bir service olduğunu belirtir
public class UserDetailsServiceImpl implements UserDetailsService {
    // UserDetailsService, Spring Security için kullanıcı detaylarını yükleme işlevselliği sağlar

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Kullanıcı adına göre veritabanından kullanıcıyı bul
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

        // Spring Security'nin kullanabileceği UserDetails nesnesini döndür
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()  // Boş yetki listesi
        );
    }
}