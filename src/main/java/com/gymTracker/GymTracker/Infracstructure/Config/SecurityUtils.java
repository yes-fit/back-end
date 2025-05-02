package com.gymTracker.GymTracker.Infracstructure.Config;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
        public static String getCurrentUserEmail() {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
}
