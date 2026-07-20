package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "taskStatus", expression = "java(getTaskStatus(dto.getStatus()))")
    @Mapping(target = "assignee", expression = "java(getAssignee(dto.getAssignee_id()))")
    @Mapping(target = "labels", expression = "java(getLabels(dto.getLabelIds()))")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assignee_id", source = "assignee.id")
    @Mapping(target = "labelIds", expression = "java(getLabelIds(model))")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "taskStatus", expression = "java(getTaskStatus(dto.getStatus().orElse(null)))")
    @Mapping(target = "assignee", expression = "java(getAssignee(dto.getAssignee_id().orElse(null)))")
    @Mapping(target = "labels", expression = "java(updateLabels(dto.getLabelIds(), model))")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    protected TaskStatus getTaskStatus(String slug) {
        return slug != null ? taskStatusRepository.findBySlug(slug).orElse(null) : null;
    }

    protected User getAssignee(Long id) {
        return id != null ? userRepository.findById(id).orElse(null) : null;
    }

    protected Set<Label> getLabels(Set<Long> labelIds) {
        if (labelIds == null || labelIds.isEmpty()) {
            return new HashSet<>();
        }
        return labelIds.stream()
                .map(id -> labelRepository.findById(id).orElse(null))
                .filter(label -> label != null)
                .collect(Collectors.toSet());
    }

    protected Set<Long> getLabelIds(Task task) {
        return task.getLabels().stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }

    protected Set<Label> updateLabels(JsonNullable<Set<Long>> labelIds, Task task) {
        if (labelIds == null || !labelIds.isPresent()) {
            return task.getLabels();
        }
        Set<Long> ids = labelIds.get();
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return ids.stream()
                .map(id -> labelRepository.findById(id).orElse(null))
                .filter(label -> label != null)
                .collect(Collectors.toSet());
    }
}