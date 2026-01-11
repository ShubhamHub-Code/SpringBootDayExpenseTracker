package com.fullstack.util;

import com.fullstack.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityUtil {

    public static void checkAdminAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new UnauthorizedException("Admin access required to perform this action");
        }
    }
}
