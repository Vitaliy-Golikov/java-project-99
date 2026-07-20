package hexlet.code.app.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusUpdateDTO {
    @Size(min = 1, message = "Название должно содержать минимум 1 символ")
    private JsonNullable<String> name = JsonNullable.undefined();

    @Size(min = 1, message = "Слаг должен содержать минимум 1 символ")
    private JsonNullable<String> slug = JsonNullable.undefined();
}
