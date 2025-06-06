package fr.upsaclay.easychair.config;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/h2-console/**"),
                                new AntPathRequestMatcher("/public/**")
                        )
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/public/**").permitAll()
                        .requestMatchers("/", "/conference", "/conference/searchConferences",
                                "/login", "/register", "/post-login").permitAll()
                        // Conference endpoints
                        .requestMatchers("/conference/ajouterConference", "/conference/myConference",
                                "/conference/myNotification", "/conference/myRoleRequests",
                                "/conference/requestRole/**", "/conference/conferenceDetail/**").authenticated()
                        .requestMatchers("/conference/deleteConference/**", "/conference/conference/{id}",
                                "/conference/update", "/conference/acceptRoleRequest/**",
                                "/conference/rejectRoleRequest/**").hasRole("ORGANIZER")
                        // Submission endpoints
                        .requestMatchers("/submissions/ajouterSubmission", "/submissions/user/**",
                                "/submissions/save", "/submissions/modifierSubmission",
                                "/submissions/update").hasRole("AUTHOR")
                        .requestMatchers("/submissions/conference/**").authenticated()
                        .requestMatchers("/submissions/submissionDetail/**").hasAnyRole("AUTHOR", "REVIEWER", "ORGANIZER")
                        // Evaluation endpoints
                        .requestMatchers("/evaluations/**").hasRole("REVIEWER")
                        // Admin endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Catch-all
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