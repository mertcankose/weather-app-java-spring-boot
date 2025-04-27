package com.mertcan.weatherapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity  // JPA anotasyonu, bu sınıfın bir veritabanı tablosunu temsil ettiğini belirtir
@Table(name = "users")  // Veritabanında oluşturulacak tablonun adı
@Data  // Lombok anotasyonu, getter/setter metodlarını otomatik oluşturur
@NoArgsConstructor  // Parametresiz constructor
@AllArgsConstructor  // Tüm alanlar için constructor
public class User {
    @Id  // Bu alanın primary key olduğunu belirtir
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Otomatik artan ID
    private Long id;

    @Column(unique = true, nullable = false)  // username alanının unique ve not null olması gerektiğini belirtir
    private String username;

    @Column(nullable = false)  // password alanının not null olması gerektiğini belirtir
    private String password;
}