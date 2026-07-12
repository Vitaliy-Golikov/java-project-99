package hexlet.code.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    @ResponseStatus(HttpStatus.OK)
    public String welcome() {
        String welcomeText = "Welcome to Spring";

        return welcomeText;
    }
}
