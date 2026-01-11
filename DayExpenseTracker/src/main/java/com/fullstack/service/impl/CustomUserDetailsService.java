
package com.fullstack.service.impl;

import com.fullstack.entity.Users;
import com.fullstack.exception.UserNotFoundException;
import com.fullstack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

   @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username).orElseThrow(()-> new UserNotFoundException("USER NOT FOUND"));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        // Assuming your Users entity has a `role` field like "ADMIN"
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())  // must prefix ROLE_
        );

        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}

