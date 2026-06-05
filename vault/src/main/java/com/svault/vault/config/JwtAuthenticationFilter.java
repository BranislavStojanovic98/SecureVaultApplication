package com.svault.vault.config;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.svault.vault.models.dto.UserDTO;
import com.svault.vault.redis.TokenBlacklistService;
import com.svault.vault.services.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    
    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = extractJwt(request);
        String jti = null;
        String username = null;



        if(StringUtils.isNotEmpty(jwt)) {
            try {
                jti = jwtService.extractTokenId(jwt);
                username = jwtService.extractUsername(jwt);
            } catch(ExpiredJwtException e) {
                SecurityContextHolder.clearContext();

                Cookie cookieJwt = new Cookie("jwt", null);
                cookieJwt.setPath("/");
                cookieJwt.setMaxAge(0);
                response.addCookie(cookieJwt);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("TOKEN_EXPIRED");
                return;
            }

            if (blacklistService.isBlackListed(jti)) {
                SecurityContextHolder.clearContext();

                Cookie cookieJwt = new Cookie("jwt", null);
                cookieJwt.setPath("/");
                cookieJwt.setMaxAge(0);
                response.addCookie(cookieJwt);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("TOKEN_REVOKED");
                return;
            }
        }

        if(username != null)
        {
            UserDTO userCheck = userService.getUserByUsername(username);

            if(userCheck != null && Boolean.TRUE.equals(userCheck.isFrozen())) {
                SecurityContextHolder.clearContext();

                Cookie cookieJwt = new Cookie("jwt", null);
                cookieJwt.setPath("/");
                cookieJwt.setMaxAge(0);
                response.addCookie(cookieJwt);

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("ACCOUNT_FROZEN");
                return;
            }
        }

        if(StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);


            if(jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    public String extractJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(StringUtils.isNotEmpty(authHeader) || StringUtils.startsWith(authHeader, "Bearer")) {
            return authHeader.substring(7);
        }

        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}