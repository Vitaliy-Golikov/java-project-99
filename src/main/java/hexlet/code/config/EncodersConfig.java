package hexlet.code.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import hexlet.code.component.RsaKeyProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Slf4j
@Configuration
public class EncodersConfig {

    private final RsaKeyProperties rsaKeys;

    public EncodersConfig(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    @PostConstruct
    public void init() {
        log.info("=== ENCODERS CONFIG INIT ===");
        log.info("RsaKeyProperties injected: {}", rsaKeys != null ? "YES" : "NO");
        log.info("============================");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("Creating PasswordEncoder (BCrypt)");
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        log.debug("=== Creating JwtEncoder ===");
        log.debug("Public key for encoder: {}", rsaKeys.getPublicKey() != null ? "PRESENT" : "NULL");
        log.debug("Private key for encoder: {}", rsaKeys.getPrivateKey() != null ? "PRESENT" : "NULL");

        if (rsaKeys.getPublicKey() == null) {
            throw new RuntimeException("Public key is NULL! Cannot create JwtEncoder");
        }
        if (rsaKeys.getPrivateKey() == null) {
            throw new RuntimeException("Private key is NULL! Cannot create JwtEncoder");
        }

        JWK jwk = new RSAKey.Builder(rsaKeys.getPublicKey()).privateKey(rsaKeys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        log.debug("=== Creating JwtDecoder ===");
        log.debug("Public key for decoder: {}", rsaKeys.getPublicKey() != null ? "PRESENT" : "NULL");

        if (rsaKeys.getPublicKey() == null) {
            log.error("Public key is NULL! Cannot create JwtDecoder");
            throw new RuntimeException("Public key is NULL! Cannot create JwtDecoder");
        }

        return NimbusJwtDecoder.withPublicKey(rsaKeys.getPublicKey()).build();
    }
}