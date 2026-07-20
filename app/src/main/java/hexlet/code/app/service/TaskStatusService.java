package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private TaskRepository taskRepository;  // ← Добавить!

    public List<TaskStatusDTO> getAll() {
        return taskStatusRepository.findAll().stream()
                .map(taskStatusMapper::map)
                .collect(Collectors.toList());
    }

    public TaskStatusDTO getById(Long id) {
        var status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus not found: " + id));
        return taskStatusMapper.map(status);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        var status = taskStatusMapper.map(dto);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    public TaskStatusDTO update(TaskStatusUpdateDTO dto, Long id) {
        var status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus not found: " + id));

        taskStatusMapper.update(dto, status);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    public void delete(Long id) {
        var status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus not found: " + id));

        if (taskRepository.existsByTaskStatusId(id)) {
            throw new RuntimeException("Cannot delete status with associated tasks");
        }

        taskStatusRepository.deleteById(id);
    }
}
