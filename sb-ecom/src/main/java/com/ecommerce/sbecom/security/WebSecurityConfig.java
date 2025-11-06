package com.ecommerce.sbecom.security;

import com.ecommerce.sbecom.security.jwt.AuthEntryPoint;
import com.ecommerce.sbecom.security.jwt.AuthTokenFilter;
import com.ecommerce.sbecom.security.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security Configuration Class
 * Ye class e-commerce application ki security ko configure karti hai
 * JWT-based authentication aur role-based authorization implement karta hai
 */
@Configuration // Spring ko batata hai ki ye configuration class hai
@EnableWebSecurity // Spring Security ko enable karta hai
//@EnableMethodSecurity // Method level security (@PreAuthorize, @Secured) enable karta hai - currently commented
@RequiredArgsConstructor // Lombok: final fields ke liye constructor automatically generate karta hai
public class WebSecurityConfig {

    // ====== DEPENDENCY INJECTION (Constructor-based) ======
    // Ye teen dependencies Spring automatically inject karega startup pe

    /**
     * UserDetailsService implementation - Database se user details fetch karta hai
     * Login authentication ke time user ko load karne ke liye use hota hai
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * AuthEntryPoint - Unauthorized access handle karta hai
     * Jab koi bina authentication ke secured endpoint access kare, ye 401 error return karta hai
     */
    private final AuthEntryPoint authEntryPoint;

    /**
     * JWT Token Filter - Har request pe JWT token validate karta hai
     * Token se user information extract karke SecurityContext mein set karta hai
     */
    private final AuthTokenFilter authTokenFilter;

    // ====== AUTHENTICATION PROVIDER BEAN ======

    /**
     * DaoAuthenticationProvider Bean
     * Ye provider database se user authentication handle karta hai
     * <p>
     * Flow:
     * 1. User credentials lega (username/password)
     * 2. UserDetailsService se user ko load karega
     * 3. PasswordEncoder se password verify karega
     * 4. Success hone pe Authentication object return karega
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        // UserDetailsService set karo - database se user load karne ke liye
        authenticationProvider.setUserDetailsService(userDetailsService);

        // PasswordEncoder set karo - password comparison ke liye
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    // ====== AUTHENTICATION MANAGER BEAN ======

    /**
     * AuthenticationManager Bean
     * Ye authentication process ko coordinate karta hai
     * <p>
     * Use case: Login endpoint mein manually authentication trigger karne ke liye
     * Example: authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(...))
     * <p>
     * Ye internally DaoAuthenticationProvider ko call karega
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ====== PASSWORD ENCODER BEAN ======

    /**
     * PasswordEncoder Bean - BCrypt algorithm use karta hai
     * <p>
     * Purpose:
     * 1. Registration time: Plain password ko encrypt karke database mein save karna
     * 2. Login time: User dwara enter kiya password ko encrypted password se compare karna
     * <p>
     * BCrypt features:
     * - Salted hashing (har password ka unique hash)
     * - Slow algorithm (brute force attacks ko difficult banata hai)
     * - Industry standard for password hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ====== SECURITY FILTER CHAIN BEAN ======

    /**
     * SecurityFilterChain Bean - Main security configuration
     * Ye define karta hai ki kaun sa endpoint secure hai aur kaun sa public
     * <p>
     * Ye method Spring Security 6 ka modern approach hai
     * Old: WebSecurityConfigurerAdapter (deprecated)
     * New: SecurityFilterChain bean (current approach)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // ====== CSRF PROTECTION DISABLE ======
        /**
         * CSRF (Cross-Site Request Forgery) protection disable kiya
         * Reason: JWT-based stateless authentication use kar rahe hain
         *
         * Note: Agar session-based auth hota, toh CSRF enable rakhna chahiye
         * REST APIs ke liye generally CSRF disable hi hota hai
         */
        http.csrf(AbstractHttpConfigurer::disable);

        // ====== EXCEPTION HANDLING ======
        /**
         * Unauthorized access handling
         * Jab koi user bina authentication ke secured resource access kare,
         * AuthEntryPoint ke through proper error response bhejta hai (401 Unauthorized)
         */
        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(authEntryPoint)
        );

        // ====== SESSION MANAGEMENT ======
        /**
         * Session policy: STATELESS
         *
         * Matlab:
         * - Server pe koi session store nahi hoga
         * - Har request mein JWT token bhejni padegi
         * - Session cookies use nahi honge
         *
         * Benefits:
         * - Scalable (server stateless rahega)
         * - Microservices friendly
         * - Load balancing easy
         */
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // ====== AUTHORIZATION RULES ======
        /**
         * URL-based access control
         * Define karta hai ki kaun sa endpoint public hai aur kaun sa secured
         */
        http.authorizeHttpRequests(authorizeRequest ->
                authorizeRequest
                        // Public endpoints - Koi bhi access kar sakta hai (no authentication required)
                        .requestMatchers("/api/auth/**").permitAll()          // Login/Register endpoints
                        .requestMatchers("/v3/api-docs/**").permitAll()       // Swagger/OpenAPI documentation
                        .requestMatchers("/swagger-ui/**").permitAll()        // Swagger UI
                        .requestMatchers("/api.public/**").permitAll()        // Public API endpoints
                        .requestMatchers("/api/admin/**").permitAll()         // Admin endpoints (currently public - SECURITY RISK!)
                        .requestMatchers("/api/test/**").permitAll()          // Test endpoints
                        .requestMatchers("/images/**").permitAll()            // Static image files

                        // Secured endpoints - Authentication required
                        .anyRequest().authenticated()                          // Baaki sab endpoints authenticated users ke liye
        );

        // ====== AUTHENTICATION PROVIDER ======
        /**
         * Custom authentication provider register karo
         * Ye DaoAuthenticationProvider use karega user authentication ke liye
         */
        http.authenticationProvider(authenticationProvider());

        // ====== JWT FILTER ======
        /**
         * JWT Authentication Filter add karo
         *
         * Position: UsernamePasswordAuthenticationFilter SE PEHLE
         *
         * Flow:
         * 1. Request aati hai
         * 2. AuthTokenFilter pehle execute hota hai
         * 3. JWT token extract aur validate karta hai
         * 4. Valid token se user details nikaal kar SecurityContext mein set karta hai
         * 5. Phir baaki filters aur controller execute hote hain
         *
         * Agar token invalid hai, toh AuthEntryPoint 401 error return karega
         */
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // Security filter chain build karke return karo
        return http.build();
    }

    // ====== WEB SECURITY CUSTOMIZER BEAN ======

    /**
     * WebSecurityCustomizer Bean
     * <p>
     * Purpose: Kuch specific paths ko COMPLETELY IGNORE karna
     * Matlab: In paths pe koi bhi security filter chain run nahi hoga
     * <p>
     * Use case: Static resources, Swagger documentation
     * <p>
     * Note: Ye paths Spring Security se completely bypass ho jayengi
     * Generally modern approach mein permitAll() hi use karte hain
     * Ye bean mostly backward compatibility ke liye hai
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(
                "/v2/api-docs",              // Swagger v2 docs (if using)
                "/configuration/ui",          // Swagger UI configuration
                "/swagger-resources/**",      // Swagger resources
                "/configuration/security",    // Security configuration
                "/swagger-ui.html",           // Old Swagger UI
                "webjars/**"                  // WebJars (frontend libraries)
        ));
    }
}
