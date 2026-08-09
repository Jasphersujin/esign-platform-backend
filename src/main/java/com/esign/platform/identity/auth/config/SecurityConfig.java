package com.esign.platform.identity.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint
            jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

                /*
                 * JWT APIs are stateless.
                 */
                .csrf(csrf ->
                        csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))

                /*
                 * Authorization rules
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Login does NOT require JWT.
                         */
                        .requestMatchers(
                                "/api/v1/users/login"
                        ).permitAll()

                        /*
                         * Everything else requires authentication.
                         */
                        .anyRequest().authenticated()
                )

                /*
                 * Return 401 for unauthenticated requests.
                 */
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                jwtAuthenticationEntryPoint
                        ))

                /*
                 * Disable Basic Authentication.
                 */
                .httpBasic(httpBasic ->
                        httpBasic.disable())

                /*
                 * Disable form login.
                 */
                .formLogin(formLogin ->
                        formLogin.disable())

                /*
                 * JWT filter runs before the normal
                 * username/password authentication filter.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}