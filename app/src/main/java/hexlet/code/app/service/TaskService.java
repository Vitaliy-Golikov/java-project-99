package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskFilterDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    public List<TaskDTO> getAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::map)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getAllWithFilters(TaskFilterDTO filter) {
        List<Task> tasks = taskRepository.findAllWithFilters(
                filter.getTitleCont(),
                filter.getAssigneeId(),
                filter.getStatus(),
                filter.getLabelId()
        );
        return tasks.stream()
                .map(taskMapper::map)
                .collect(Collectors.toList());
    }

    public TaskDTO getById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        return taskMapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO dto) {
        var task = taskMapper.map(dto);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO update(TaskUpdateDTO dto, Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));

        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found: " + id);
        }
        taskRepository.deleteById(id);
    }
}
