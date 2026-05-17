package com.svault.vault.audit;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.svault.vault.services.AuditLogService;
import com.svault.vault.services.UserService;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditLogService auditLogService;
    private final UserService userService;

    @AfterReturning("@annotation(auditedAction)")
    public void logAction(JoinPoint joinPoint,
                          AuditedAction auditedAction) {

        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null ||
                !auth.isAuthenticated() ||
                auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
                return;
            }

            Object principal = auth.getPrincipal();

            if (principal instanceof String) {
                return;
            }

            String action = auditedAction.value();

            UserDetails user = (UserDetails) auth.getPrincipal();

            String username = user.getUsername();
            Integer userId = userService.getUserByUsername(username).getId();

            String payload =
                    "action=" + action +
                    ",args=" + Arrays.toString(joinPoint.getArgs()) +
                    ",user=" + username;

            auditLogService.createLog(userId, payload);

        } catch (Exception e) {
            System.err.println("Audit failed: " + e.getMessage());
        }
    }
}
