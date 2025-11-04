package com.secure.notes.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
/*
* If we want to access all the publicly available pages we do not have to add all the enpoints,
* in the requestMatchers rather we can have our endpoints start with /public.
* for example -
* /public/login or /public/contact
* then we can tell the requestMatchers to permit access to all the endpoints that start with /public.
* */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
       // http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager =
                new InMemoryUserDetailsManager();
        if (!manager.userExists("user1")) {
            manager.createUser(
                    User.withUsername("user1")
                            .password("{noop}password1")
                            .roles("USER")
                            .build()
            );
        }
        if (!manager.userExists("admin")) {
            manager.createUser(
                    User.withUsername("admin")
                            .password("{noop}adminPass")
                            .roles("ADMIN")
                            .build()
            );
        }
        return manager;
    }
}

/*
*     SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/contact").permitAll()
                .requestMatchers("/public/**").permitAll() // publicly available endpoints.
                .requestMatchers("/admin").denyAll() // this is used to deny any connection or access to the specific endpoint in our case "/admin".
                .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
       // http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }
*
* */
