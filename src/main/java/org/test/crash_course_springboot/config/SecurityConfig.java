package org.test.crash_course_springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.test.crash_course_springboot.security.JwtFilter;
import org.test.crash_course_springboot.services.CustomUserDetailsService;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


//                .authorizeHttpRequests(authz -> authz
//
//                        // public endpoints
//                        .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/user/createstudent").permitAll()
//
//                        // ADMIN only: get all students
//
//
//                        // other student APIs require login (USER or ADMIN)
//                        .requestMatchers("/user/**").authenticated()
//
//                        // everything else
//                        .anyRequest().permitAll()
//                )
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authz -> authz
                        // public endpoints
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/createstudent").permitAll()

                        // USER or ADMIN can access other student APIs
                        .requestMatchers("/student/**").authenticated()

                        // everything else
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService(){//(PasswordEncoder passwordEncoder){
//        UserDetails user= User.withUsername("Alice").password(passwordEncoder.encode("alice123")).roles("USER").build();
//        UserDetails admin=User.withUsername("Bob").password(passwordEncoder.encode("admin123")).roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(user,admin);
        return new CustomUserDetailsService();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider authProvider= new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        return new ProviderManager(List.of(authenticationProvider(userDetailsService, passwordEncoder)));
    }
}