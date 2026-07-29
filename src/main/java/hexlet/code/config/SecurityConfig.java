package hexlet.code.config;

import hexlet.code.component.RsaKeyProperties;
import hexlet.code.service.CustomUserDetailsService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(proxyTargetClass = true)
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userService;
    private final JwtDecoder jwtDecoder;
    private final RsaKeyProperties rsaKeys;
    private final String activeProfile;

    public SecurityConfig(PasswordEncoder passwordEncoder,
                          CustomUserDetailsService userService,
                          JwtDecoder jwtDecoder,
                          RsaKeyProperties rsaKeys,
                          @Value("${spring.profiles.active:default}") String activeProfile) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtDecoder = jwtDecoder;
        this.rsaKeys = rsaKeys;
        this.activeProfile = activeProfile;
    }

    @PostConstruct
    public void init() {
        log.info("=== SECURITY CONFIG INIT ===");
        log.info("Active profile: {}", activeProfile);
        log.info("UserService: {}", userService != null ? "PRESENT" : "NULL");
        log.info("PasswordEncoder: {}", passwordEncoder != null ? "PRESENT" : "NULL");
        log.info("JwtDecoder: {}", jwtDecoder != null ? "PRESENT" : "NULL");
        log.info("RsaKeyProperties: {}", rsaKeys != null ? "PRESENT" : "NULL");
        log.info("============================");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("Creating SecurityFilterChain with JwtDecoder: {}",
                jwtDecoder != null ? "PRESENT" : "NULL");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Публичные эндпоинты
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // ← ДОБАВЛЕНО
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/index.html").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((rs) -> rs.jwt((jwt) -> jwt.decoder(jwtDecoder)))
                .httpBasic(Customizer.withDefaults());

        log.debug("SecurityFilterChain created successfully");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.debug("Creating AuthenticationManager");
        return config.getAuthenticationManager();
    }
}