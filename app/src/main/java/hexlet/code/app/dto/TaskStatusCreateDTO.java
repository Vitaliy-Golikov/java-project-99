package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCreateDTO {
    @NotBlank(message = "Название статуса обязательно")
    @Size(min = 1, message = "Название должно содержать минимум 1 символ")
    private String name;

    @NotBlank(message = "Слаг статуса обязателен")
    @Size(min = 1, message = "Слаг должен содержать минимум 1 символ")
    private String slug;
}
