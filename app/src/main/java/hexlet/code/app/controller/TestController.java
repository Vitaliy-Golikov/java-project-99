package hexlet.code.app.controller;

import io.sentry.Sentry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-sentry")
    public String testSentry() {
        try {
            throw new Exception("This is a test error for Sentry!");
        } catch (Exception e) {
            Sentry.captureException(e);
            return "Error sent to Sentry!";
        }
    }
}
