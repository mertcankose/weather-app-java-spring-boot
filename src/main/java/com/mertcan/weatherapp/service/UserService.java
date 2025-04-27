package com.mertcan.weatherapp.service;

import com.mertcan.weatherapp.exception.UserAlreadyExistsException;
import com.mertcan.weatherapp.model.User;
import com.mertcan.weatherapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List; // List sınıfını import et

@Service  // Spring anotasyonu, bu sınıfın bir service olduğunu belirtir
public class UserService {

    @Autowired  // Dependency injection, UserRepository nesnesini Spring'den alır
    private UserRepository userRepository;

    @Autowired  // Dependency injection, PasswordEncoder nesnesini Spring'den alır
    private PasswordEncoder passwordEncoder;

    // Kullanıcı oluşturma metodu
    public User createUser(String username, String password) {
        // Kullanıcı adı kontrolü
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Bu kullanıcı adı zaten kullanılıyor: " + username);
        }

        // Yeni kullanıcı oluştur
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));  // Şifreyi encode et

        // Kullanıcıyı kaydet ve döndür
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}