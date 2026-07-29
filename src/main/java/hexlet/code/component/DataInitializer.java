package hexlet.code.component;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.interfaces.LabelService;
import hexlet.code.service.interfaces.TaskStatusService;
import hexlet.code.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TaskStatusRepository taskStatusRepositoryRepository;
    private final TaskStatusService taskStatusService;
    private final LabelService labelService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting data initialization...");

        var email = "hexlet@example.com";
        var userData = new UserCreateDTO();
        userData.setEmail(email);
        userData.setPassword("qwerty");
        userService.createUser(userData);
        log.info("Created default user: {}", email);

        var statuses = Map.of(
                "draft", "Draft",
                "to_review", "Under review",
                "to_be_fixed", "Needs fixing",
                "to_publish", "Ready to publish",
                "published", "Published"
        );

        statuses.forEach((slug, name) -> {
            var dto = new TaskStatusCreateDTO();
            dto.setSlug(slug);
            dto.setName(name);
            taskStatusService.createTaskStatus(dto);
        });
        log.info("Created {} task statuses", statuses.size());

        List<String> defaultLabels = List.of("feature", "bug");
        defaultLabels.forEach(name -> {
            var label = new LabelCreateDTO();
            label.setName(name);
            labelService.createLabel(label);
        });
        log.info("Created {} default labels", defaultLabels.size());

        log.info("Data initialization completed successfully!");
    }
}