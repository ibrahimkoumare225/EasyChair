package fr.upsaclay.easychair.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable) // Disable CSRF (not recommended for production)
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/public/**").permitAll()
                        .requestMatchers("/", "/conference", "/conference/searchConferences", "/login", "/register").permitAll()
                        .requestMatchers("/conference/ajouterConference").authenticated() // All authenticated users
                        .requestMatchers("/conference/deleteConference/**", "/conference/conference/{id}").hasRole("ORGANIZER") // Organizer-only for delete/modify
                        .requestMatchers("/submissions/ajouterSubmission", "/submissions/user/**").hasRole("AUTHOR")
                        .requestMatchers("/submissions/**").hasRole("REVIEWER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((login) -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/conference", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/conference?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}