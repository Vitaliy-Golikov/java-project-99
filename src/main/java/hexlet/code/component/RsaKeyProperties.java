package hexlet.code.component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Slf4j
@Component
@ConfigurationProperties(prefix = "rsa")
@Setter
@Getter
public class RsaKeyProperties {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    @PostConstruct
    public void init() {
        log.info("=== RSA Keys loading ===");
        log.info("Public key: {}", publicKey != null ? "PRESENT (" + publicKey.getAlgorithm() + ")" : "NULL");
        log.info("Private key: {}", privateKey != null ? "PRESENT (" + privateKey.getAlgorithm() + ")" : "NULL");
        log.info("========================");
    }
}