package hexlet.code.app.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class LabelUpdateDTO {
    @Size(min = 3, max = 1000, message = "Название должно содержать от 3 до 1000 символов")
    private JsonNullable<String> name = JsonNullable.undefined();
}