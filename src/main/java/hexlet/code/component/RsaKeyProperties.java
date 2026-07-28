package hexlet.code.component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
@ConfigurationProperties(prefix = "rsa")
@Setter
@Getter
public class RsaKeyProperties {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    @PostConstruct
    public void init() {
        System.out.println("=== RSA Keys loading ===");
        System.out.println("Public key: " + (publicKey != null ? "PRESENT (" + publicKey.getAlgorithm() + ")" : "NULL"));
        System.out.println("Private key: " + (privateKey != null ? "PRESENT (" + privateKey.getAlgorithm() + ")" : "NULL"));
        System.out.println("========================");
    }
}