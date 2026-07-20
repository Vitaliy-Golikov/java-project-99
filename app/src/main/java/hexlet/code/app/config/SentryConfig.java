package hexlet.code.app.config;

import io.sentry.Sentry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class SentryConfig {

    @Value("${sentry.dsn}")
    private String sentryDsn;

    @PostConstruct
    public void initSentry() {
        Sentry.init(options -> {
            options.setDsn(sentryDsn);
            options.setEnvironment("production");
            options.setTracesSampleRate(1.0);
            options.setSendDefaultPii(true);
        });
    }
}
