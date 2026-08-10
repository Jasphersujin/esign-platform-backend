package com.esign.platform.identity.auth.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.esign.platform.identity.user.User;
import com.esign.platform.identity.user.UserRepository;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        /*
         * No Authorization header.
         *
         * Let Spring Security decide whether this
         * endpoint requires authentication.
         */
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authorizationHeader.substring(7);

        try {

            /*
             * Extract User ID from JWT
             */
            var userId =
                    jwtService.extractUserId(token);

            /*
             * Don't authenticate again if already authenticated.
             */
            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                /*
                 * Find active user
                 */
//                User user =
//                        userRepository
//                                .findByIdAndDeletedFalse(userId)
//                                .orElse(null);
            	
	            	User user =
	            	        userRepository
	            	                .findByIdAndDeletedFalseWithRole(userId)
	            	                .orElse(null);

                if (user != null
                        && Boolean.TRUE.equals(user.getActive())
                        && !Boolean.TRUE.equals(
                                user.getAccountLocked())
                        && jwtService.isTokenValid(
                                token,
                                user)) {

                    /*
                     * For now use roleCode as authority.
                     *
                     * Later we will replace this with
                     * permission-based authorities.
                     */
                    String authority =
                            "ROLE_"
                                    + user.getRole()
                                            .getRoleCode();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    Collections.singletonList(
                                            new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                                    authority
                                            )
                                    )
                            );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );
                }
            }

        } catch (JwtException | IllegalArgumentException ex) {

            /*
             * Invalid JWT.
             *
             * Don't authenticate the request.
             */
            SecurityContextHolder
                    .clearContext();
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}