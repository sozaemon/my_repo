package com.arif.hrs.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.arif.hrs.auth.filter.AuthenticationFilter;
import com.arif.hrs.auth.filter.DynamicAuthorizationManager;
import com.arif.hrs.auth.handler.AuthenticationFailHandler;
import com.arif.hrs.auth.handler.NoAccessHandler;
import com.arif.hrs.domain.service.UserAuthServiceDomain;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

  private final AuthenticationFilter authenticationFilter;
  private final UserAuthServiceDomain userAuthService;
  private final DynamicAuthorizationManager authManager;
  private final NoAccessHandler noAccessHandler;
  private final AuthenticationFailHandler authFailHandler;

  @Value("${app.allowed.cors}")
  private String[] allowedCors;

  public SecurityConfiguration(AuthenticationFilter authenticationFilter,
      UserAuthServiceDomain userAuthServiceDomain, DynamicAuthorizationManager authManager,
      NoAccessHandler noAccessHandler, AuthenticationFailHandler authenticationFailHandler) {
    this.authenticationFilter = authenticationFilter;
    this.userAuthService = userAuthServiceDomain;
    this.authManager = authManager;
    this.noAccessHandler = noAccessHandler;
    this.authFailHandler = authenticationFailHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http.csrf(c -> c.disable())
        .cors(c -> c.configurationSource(corsConfigurationSource()))
        .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/auth/login",
                "/api/auth/logout",
                "/api/auth/user-roles",
                "/api-docs",
                "/swagger-ui/index.html")
            .permitAll()
            .anyRequest().access(authManager))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(ex -> {
          ex.accessDeniedHandler(noAccessHandler);
          ex.authenticationEntryPoint(authFailHandler);
        });

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList(allowedCors));
    config.setAllowedMethods(List.of(
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.PATCH.name(),
        HttpMethod.DELETE.name()));
    config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider pr = new DaoAuthenticationProvider(userAuthService);
    pr.setPasswordEncoder(passwordEncoder());
    return pr;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
    return config.getAuthenticationManager();
  }
}