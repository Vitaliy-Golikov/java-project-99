package hexlet.code.service.impls;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.interfaces.TaskService;
import hexlet.code.specification.TaskSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification specBuilder;

    @Override
    public TaskDTO getTaskById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + id + " does not exist!"));
        return taskMapper.map(task);
    }

    @Override
    public List<TaskDTO> getAllTasks(TaskParamsDTO params) {
        var spec = specBuilder.build(params);
        var tasks = taskRepository.findAll(spec);
        return tasks.stream()
                .map(taskMapper::map)
                .toList();
    }

    @Override
    @Transactional
    public TaskDTO createTask(TaskCreateDTO taskData) {
        var assigneeId = taskData.getAssigneeId();
        var slug = taskData.getStatus();
        var labelIds = taskData.getTaskLabelIds();

        var task = taskMapper.map(taskData);

        if (assigneeId != null) {
            var user = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new ResourceNotFoundException("User with id: "
                            + assigneeId + " does not exist!"));
            task.setAssignee(user);
        }

        var status = taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with slug: "
                        + slug + " does not exist!"));
        task.setTaskStatus(status);

        task.setLabels(getLabelsByIds(labelIds));

        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @Override
    @Transactional
    public TaskDTO updateTask(TaskUpdateDTO taskData, Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + id + " does not exist!"));

        // Обновляем простые поля через маппер
        taskMapper.update(taskData, task);

        // Обработка assigneeId
        if (taskData.getAssigneeId() != null && taskData.getAssigneeId().isPresent()) {
            Long assigneeId = taskData.getAssigneeId().get();
            if (assigneeId != null) {
                var user = userRepository.findById(assigneeId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "User with id: " + assigneeId + " does not exist!"));
                task.setAssignee(user);
            } else {
                task.setAssignee(null);
            }
        }

        // Обработка статуса
        if (taskData.getStatus() != null && taskData.getStatus().isPresent()) {
            String statusSlug = taskData.getStatus().get();
            if (statusSlug != null && !statusSlug.isBlank()) {
                var status = taskStatusRepository.findBySlug(statusSlug)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Task status with slug: " + statusSlug + " does not exist!"));
                task.setTaskStatus(status);
            } else {
                task.setTaskStatus(null);
            }
        }

        // Обработка labels
        if (taskData.getTaskLabelIds() != null && taskData.getTaskLabelIds().isPresent()) {
            var labelIds = taskData.getTaskLabelIds().get();
            if (labelIds != null && !labelIds.isEmpty()) {
                task.setLabels(getLabelsByIds(labelIds));
            } else {
                task.setLabels(new HashSet<>());
            }
        }

        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private Set<Label> getLabelsByIds(List<Long> labelIds) {
        if (labelIds == null || labelIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(labelRepository.findAllById(labelIds));
    }
}