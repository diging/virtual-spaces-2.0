package edu.asu.diging.vspace.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import edu.asu.diging.simpleusers.core.service.SimpleUsersConstants;

@Configuration
@EnableWebSecurity
public class SecurityContext {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Spring Security ignores request to static resources such as CSS or JS
        // files.
        return web -> web.ignoring().requestMatchers("/resources/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding(StandardCharsets.UTF_8.name());
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);

        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error=bad_credentials"))
                // Configures the logout function
                .logout(logout -> logout
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/"))
                .exceptionHandling(ex -> ex.accessDeniedPage("/403"))
                // Configures url based authorization
                .authorizeHttpRequests(auth -> auth
                        // Allow internal forwards/error dispatches (Spring Security 6 secures
                        // every dispatcher type by default)
                        .dispatcherTypeMatchers(jakarta.servlet.DispatcherType.FORWARD,
                                jakarta.servlet.DispatcherType.ERROR).permitAll()
                        // Anyone can access the urls
                        .requestMatchers("/", "/exhibit/**", "/api/**", "/resources/**", "/login",
                                "/logout", "/register", "/reset/**", "/setup/admin", "/404", "/preview/**").permitAll()
                        // The rest of the our application is protected.
                        .requestMatchers("/users/**", "/admin/**", "/staff/user/**").hasRole("ADMIN")
                        .requestMatchers("/staff/**").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers("/password/**").hasRole(SimpleUsersConstants.CHANGE_PASSWORD_ROLE)
                        .anyRequest().hasRole("USER"));
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

}
