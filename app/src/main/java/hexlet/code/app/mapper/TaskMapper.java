package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Mapping(target = "taskStatus", expression = "java(getTaskStatus(dto.getStatus()))")
    @Mapping(target = "assignee", expression = "java(getAssignee(dto.getAssignee_id()))")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assignee_id", source = "assignee.id")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "taskStatus", expression = "java(getTaskStatus(dto.getStatus().orElse(null)))")
    @Mapping(target = "assignee", expression = "java(getAssignee(dto.getAssignee_id().orElse(null)))")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    protected TaskStatus getTaskStatus(String slug) {
        return slug != null ? taskStatusRepository.findBySlug(slug).orElse(null) : null;
    }

    protected User getAssignee(Long id) {
        return id != null ? userRepository.findById(id).orElse(null) : null;
    }
}