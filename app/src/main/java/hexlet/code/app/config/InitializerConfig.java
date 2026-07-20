package hexlet.code.app.config;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InitializerConfig {
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;  // ← Добавить
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            // Создание администратора
            if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("hexlet@example.com");
                admin.setPassword(passwordEncoder.encode("qwerty"));
                admin.setFirstName("Admin");
                admin.setLastName("Hexlet");
                userRepository.save(admin);
                System.out.println("Admin user created!");
            }

            // Создание дефолтных статусов
            List<String> defaultSlugs = List.of("draft", "to_review", "to_be_fixed", "to_publish", "published");
            for (String slug : defaultSlugs) {
                if (taskStatusRepository.findBySlug(slug).isEmpty()) {
                    TaskStatus status = new TaskStatus();
                    status.setSlug(slug);
                    status.setName(convertSlugToName(slug));
                    taskStatusRepository.save(status);
                    System.out.println("TaskStatus created: " + slug);
                }
            }
        };
    }

    private String convertSlugToName(String slug) {
        return switch (slug) {
            case "draft" -> "Draft";
            case "to_review" -> "To Review";
            case "to_be_fixed" -> "To Be Fixed";
            case "to_publish" -> "To Publish";
            case "published" -> "Published";
            default -> slug;
        };
    }
}