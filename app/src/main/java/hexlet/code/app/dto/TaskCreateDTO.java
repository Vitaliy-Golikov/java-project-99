package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {
    @NotBlank(message = "Название задачи обязательно")
    @Size(min = 1, message = "Название должно содержать минимум 1 символ")
    private String title;

    private String content;

    private Integer index;

    @NotNull(message = "Статус обязателен")
    private String status;  // slug статуса

    private Long assignee_id;
}