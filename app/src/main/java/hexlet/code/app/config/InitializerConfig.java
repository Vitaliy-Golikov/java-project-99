package hexlet.code.app.config;

import hexlet.code.app.model.Label;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class InitializerConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;  // ← Добавить

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            // Создание администратора
            if (!userRepository.findByEmail("hexlet@example.com").isPresent()) {
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
                if (!taskStatusRepository.findBySlug(slug).isPresent()) {
                    TaskStatus status = new TaskStatus();
                    status.setSlug(slug);
                    status.setName(convertSlugToName(slug));
                    taskStatusRepository.save(status);
                    System.out.println("TaskStatus created: " + slug);
                }
            }

            List<String> defaultLabels = List.of("feature", "bug");
            for (String name : defaultLabels) {
                if (!labelRepository.findByName(name).isPresent()) {
                    Label label = new Label();
                    label.setName(name);
                    labelRepository.save(label);
                    System.out.println("Label created: " + name);
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