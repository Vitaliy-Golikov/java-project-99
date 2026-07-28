package hexlet.code.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import hexlet.code.component.RsaKeyProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class EncodersConfig {

    @Autowired
    private RsaKeyProperties rsaKeys;

    @PostConstruct
    public void init() {
        System.out.println("=== ENCODERS CONFIG INIT ===");
        System.out.println("RsaKeyProperties injected: " + (rsaKeys != null ? "YES" : "NO"));
        System.out.println("============================");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("=== Creating PasswordEncoder (BCrypt) ===");
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        System.out.println("=== Creating JwtEncoder ===");
        System.out.println("Public key for encoder: " + (rsaKeys.getPublicKey() != null ? "PRESENT" : "NULL"));
        System.out.println("Private key for encoder: " + (rsaKeys.getPrivateKey() != null ? "PRESENT" : "NULL"));

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
        System.out.println("=== Creating JwtDecoder ===");
        System.out.println("Public key for decoder: " + (rsaKeys.getPublicKey() != null ? "PRESENT" : "NULL"));

        if (rsaKeys.getPublicKey() == null) {
            System.err.println("ERROR: Public key is NULL! Cannot create JwtDecoder");
            throw new RuntimeException("Public key is NULL! Cannot create JwtDecoder");
        }

        return NimbusJwtDecoder.withPublicKey(rsaKeys.getPublicKey()).build();
    }
}