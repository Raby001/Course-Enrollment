package enroll_management.enroll_management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    // 🔐 Password Encoder - BCrypt is secure and recommended
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🛡️ Main Security Filter Chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for simplicity (enable later in production)
            .csrf(AbstractHttpConfigurer::disable)

            // Configure login page
            .formLogin(form -> form
                .loginPage("/login")           // Custom login page URL
                .loginProcessingUrl("/login")  // Where form submits
                .defaultSuccessUrl("/home", true) // After login, go here
                .failureUrl("/login?error=true")      // On fail, show error
                .permitAll()
            )

            // Configure logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            // Define which URLs require authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/student/**").hasRole("STUDENT")
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
