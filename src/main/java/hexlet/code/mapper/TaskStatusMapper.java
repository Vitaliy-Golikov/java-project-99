package hexlet.code.mapper;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = { JsonNullableMapper.class },
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd")
    public abstract TaskStatus map(TaskStatusDTO taskData);

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd")
    public abstract TaskStatusDTO map(TaskStatus taskStatus);

    public abstract TaskStatus map(TaskStatusCreateDTO data);

    // Конкретный метод
    public void update(TaskStatusUpdateDTO taskStatusData, @MappingTarget TaskStatus taskStatus) {
        if (taskStatusData.getName() != null && taskStatusData.getName().isPresent()) {
            taskStatus.setName(taskStatusData.getName().get());
        }
        if (taskStatusData.getSlug() != null && taskStatusData.getSlug().isPresent()) {
            taskStatus.setSlug(taskStatusData.getSlug().get());
        }
    }
}