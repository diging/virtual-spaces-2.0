package edu.asu.diging.vspace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
        // Spring Security ignores request to static resources such as CSS or JS
        // files.
        .ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/?error=bad_credentials")
                // Configures the logout function
                .and()
                .csrf()
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and().exceptionHandling().accessDeniedPage("/403")
                // Configures url based authorization
                .and()
                .authorizeRequests()
                // Anyone can access the urls
                .antMatchers("/", "/exhibit/**", "/api/**", "/resources/**", "/login",
                        "/logout", "/register").permitAll()
                // The rest of the our application is protected.
                .antMatchers("/users/**", "/admin/**", "/staff/user/**").hasRole("ADMIN")
                .antMatchers("/staff/**").hasAnyRole("STAFF", "ADMIN")
                .anyRequest().hasRole("USER");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

}