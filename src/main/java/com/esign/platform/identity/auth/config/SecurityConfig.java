//package com.esign.platform.identity.auth.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    private final JwtAuthenticationEntryPoint
//            jwtAuthenticationEntryPoint;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http)
//            throws Exception {
//
//        http
//
//                /*
//                 * JWT APIs are stateless.
//                 */
//                .csrf(csrf ->
//                        csrf.disable())
//
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(
//                                SessionCreationPolicy.STATELESS
//                        ))
//
//                /*
//                 * Authorization rules
//                 */
//                .authorizeHttpRequests(auth -> auth
//
//                        /*
//                         * Login does NOT require JWT.
//                         */
//                        .requestMatchers(
//                                "/api/v1/users/login"
//                        ).permitAll()
//
//                        /*
//                         * Everything else requires authentication.
//                         */
//                        .anyRequest().authenticated()
//                )
//
//                /*
//                 * Return 401 for unauthenticated requests.
//                 */
//                .exceptionHandling(exception ->
//                        exception.authenticationEntryPoint(
//                                jwtAuthenticationEntryPoint
//                        ))
//
//                /*
//                 * Disable Basic Authentication.
//                 */
//                .httpBasic(httpBasic ->
//                        httpBasic.disable())
//
//                /*
//                 * Disable form login.
//                 */
//                .formLogin(formLogin ->
//                        formLogin.disable())
//
//                /*
//                 * JWT filter runs before the normal
//                 * username/password authentication filter.
//                 */
//                .addFilterBefore(
//                        jwtAuthenticationFilter,
//                        UsernamePasswordAuthenticationFilter.class
//                );
//
//        return http.build();
//    }
//}


package com.esign.platform.identity.auth.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            /*
             * Enable CORS
             */
            .cors(cors -> cors
                    .configurationSource(corsConfigurationSource())
            )

            /*
             * JWT APIs are stateless
             */
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            /*
             * Authorization
             */
            .authorizeHttpRequests(auth -> auth

                    /*
                     * CORS preflight
                     */
                    .requestMatchers(
                            org.springframework.http.HttpMethod.OPTIONS,
                            "/**"
                    ).permitAll()

                    /*
                     * Login does not require JWT
                     */
                    .requestMatchers(
                            "/api/v1/users/login"
                    ).permitAll()

                    /*
                     * Everything else requires JWT
                     */
                    .anyRequest().authenticated()
            )

            /*
             * 401 response
             */
            .exceptionHandling(exception ->
                    exception.authenticationEntryPoint(
                            jwtAuthenticationEntryPoint
                    )
            )

            /*
             * Disable Basic Auth
             */
            .httpBasic(httpBasic ->
                    httpBasic.disable()
            )

            /*
             * Disable form login
             */
            .formLogin(formLogin ->
                    formLogin.disable()
            )

            /*
             * JWT filter
             */
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }


    /**
     * CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        /*
         * React frontend
         */
        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        /*
         * HTTP methods allowed from frontend
         */
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        /*
         * Request headers allowed
         */
        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type",
                        "Accept"
                )
        );

        /*
         * Response headers frontend can read
         */
        configuration.setExposedHeaders(
                List.of("Authorization")
        );

        /*
         * You are using JWT in Authorization header,
         * so cookies are not required.
         */
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}