package com.program.passholder.Security;

import com.program.passholder.Security.IPFilter.LoginLimitFilter;
import com.program.passholder.Session.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    //Konfiguracja zabezpieczeń

    //Dodanie filtra JWT dla tokenu użytkownika
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Konfiguracja zabezpieczeń HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   //TODO Wyłączona ochrona przed Cross-Site Request Forgery (CSRF).
                .httpBasic(httpBasic -> httpBasic.disable())   //TODO Wyłączone HTTP Basic Authentication - brak logowania przez przeglądarkę
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/update/**").permitAll()
                        .requestMatchers("/api/userValidation").permitAll()     //dostęp dla wszystkich tylko do procesu logowania
                        .requestMatchers("/api/CreateNewAccount").permitAll()  //dostęp dla wszystkich przy zakładaniu konta
                        .requestMatchers("/api/2FA").permitAll()
                        .requestMatchers("/api/restorePassword").permitAll()
                        .requestMatchers("/api/restorePassword/validateToken").permitAll()
                        .requestMatchers("/api/restorePassword/saveNewPassword").permitAll()
                        .requestMatchers("/api/oldPassValidation").permitAll()
                        .anyRequest().authenticated())               // Cała reszta wymaga tokenu
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);    //W pierwszej kolejności sprawdzaj dodany jwtFilter (Token użytkownika)
        return http.build();
    }

    //Importuj ustawienia Filtra limitu ilości połaczeń: IPFilter->RateLimitFilter
    @Bean  //Rejestruj Filtr w Springu
    public FilterRegistrationBean<LoginLimitFilter> rateLimitFilterRegistration(LoginLimitFilter loginLimitFilter) {
        FilterRegistrationBean<LoginLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(loginLimitFilter);
        registration.addUrlPatterns("/api/*"); // Zarejestruj dla walidacji użytkownika (/* - dla wszystkich endpointów)
        /*
        registration.addUrlPatterns("/api/userValidation");
        registration.addUrlPatterns("/api/CreateNewAccount");
        registration.addUrlPatterns("/api/2FA");
        registration.addUrlPatterns("/api/restorePassword");
        registration.addUrlPatterns("/api/restorePassword/validateToken");
        registration.addUrlPatterns("/api/restorePassword/saveNewPassword");
        */
        registration.setOrder(1); // ustala kolejność działania filtrów
        return registration;
    }
}
