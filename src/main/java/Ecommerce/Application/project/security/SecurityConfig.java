package Ecommerce.Application.project.security;

import Ecommerce.Application.project.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(req -> req

                        // ===========================
                        // PUBLIC ROUTES
                        // ===========================
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                        // Image upload + static files
                        .requestMatchers(HttpMethod.POST, "/api/files/upload").permitAll()
                        .requestMatchers("/api/files/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                        // ===========================
                        // CATEGORY
                        // ===========================
                        .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categories/**").hasAuthority("ADMIN")

                        // ===========================
                        // PRODUCT
                        // ===========================
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/products/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasAuthority("ADMIN")

                        // ===========================
                        // STOCK
                        // ===========================
                        .requestMatchers(HttpMethod.GET, "/stock").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/stock/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/stock/set").hasAuthority("ADMIN")

                        // ===========================
                        // STOCK IMPORT
                        // ===========================
                        // STOCK IMPORT (ADMIN ONLY)
                        .requestMatchers(HttpMethod.POST, "/stock/import").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/stock/imports").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/product-imports").hasAuthority("ADMIN")

                        // ===========================
                        // USER & ROLE
                        // ===========================
                        .requestMatchers("/users/**").hasAuthority("ADMIN")
                        .requestMatchers("/roles/**").hasAuthority("ADMIN")

                        // ===========================
                        // ANY OTHER SECURED
                        // ===========================
                        .anyRequest().authenticated()
                );

        // JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    // ===========================
    // CORS CONFIG
    // ===========================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg)
            throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
