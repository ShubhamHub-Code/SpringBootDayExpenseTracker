package com.fullstack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "Customer Name should be 2 character ")
    private String name;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>]).{5,}$", message = "Password must be at least 5 characters long and contain at least one special character")
    private String password;
}
