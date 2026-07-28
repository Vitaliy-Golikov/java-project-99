package hexlet.code.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper om;

    private Task testTask;
    private TaskStatus testTaskStatus;
    private User testUser;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();

        testUser = ModelGenerator.generateUser();
        userRepository.save(testUser);
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));

        // Создаем статус с уникальным именем
        testTaskStatus = ModelGenerator.generateTaskStatus();
        String uniqueName = "test_status_" + System.currentTimeMillis();
        testTaskStatus.setName(uniqueName);
        testTaskStatus.setSlug(uniqueName + "_slug");
        taskStatusRepository.save(testTaskStatus);

        testTask = ModelGenerator.generateTask();
        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);
        taskRepository.save(testTask);
    }

    // ... остальные тесты ...

    @Test
    public void testUpdateStatus() throws Exception {
        // Создаем статус с уникальным именем
        var newStatus = ModelGenerator.generateTaskStatus();
        String uniqueName = "status_" + System.currentTimeMillis();
        newStatus.setName(uniqueName);
        newStatus.setSlug(uniqueName + "_slug");
        taskStatusRepository.save(newStatus);

        var data = new HashMap<>();
        data.put("status", newStatus.getSlug());

        mockMvc.perform(put("/api/tasks/" + testTask.getId())
                        .with(token)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isOk());

        var updated = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(updated.getTaskStatus().getSlug())
                .isEqualTo(newStatus.getSlug());
    }

    @Test
    public void testFilterByStatus() throws Exception {
        // Создаем статус с уникальным именем
        var anotherStatus = ModelGenerator.generateTaskStatus();
        String uniqueName = "filter_status_" + System.currentTimeMillis();
        anotherStatus.setName(uniqueName);
        anotherStatus.setSlug(uniqueName + "_slug");
        taskStatusRepository.save(anotherStatus);

        var taskWithAnotherStatus = ModelGenerator.generateTask();
        taskWithAnotherStatus.setTaskStatus(anotherStatus);
        taskWithAnotherStatus.setAssignee(testUser);
        taskRepository.save(taskWithAnotherStatus);

        var result = mockMvc.perform(get("/api/tasks")
                        .param("status", testTaskStatus.getSlug()).with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<TaskDTO> tasks = om.readValue(body, new TypeReference<>() { });

        assertThat(tasks)
                .extracting(TaskDTO::getStatus)
                .containsOnly(testTaskStatus.getSlug());
    }

    // ... остальные тесты ...
}