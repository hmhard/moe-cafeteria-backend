package et.moe.ethernet.cateteria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final CorsConfigurationSource corsConfigurationSource;
    
    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .authorizeHttpRequests(authz -> authz
                // Swagger UI endpoints (without context path)
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/v3/api-docs/**").permitAll()
                // Auth endpoints (with context path)
                .requestMatchers("/auth/login", "/auth/logout", "/auth/me").permitAll()
                // Public API endpoints (with context path) - allow both authenticated and anonymous
                .requestMatchers("/meal-types/active").permitAll()
                .requestMatchers("/meal-categories/active").permitAll()
                .requestMatchers("/meal-categories/by-type/**").permitAll()
                .requestMatchers("/meal-categories/**").permitAll() // Allow access to individual meal categories
                .requestMatchers("/employees/by-card/**").permitAll()
                .requestMatchers("/employees/by-code/**").permitAll()
                .requestMatchers("/meal-records/record").permitAll()
                .requestMatchers("/meal-records/check-duplicate").permitAll()
                .requestMatchers("/meal-records/**/receipt").permitAll()
                .requestMatchers("/support-config").permitAll()
                .requestMatchers("/support-reports/**").permitAll()
                .requestMatchers("/api/print/**").permitAll()
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )
            .httpBasic();
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
} 