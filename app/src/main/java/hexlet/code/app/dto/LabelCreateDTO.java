package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelCreateDTO {
    @NotBlank(message = "Название метки обязательно")
    @Size(min = 3, max = 1000, message = "Название должно содержать от 3 до 1000 символов")
    private String name;
}