package hexlet.code.app.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskUpdateDTO {
    @Size(min = 1, message = "Название должно содержать минимум 1 символ")
    private JsonNullable<String> title = JsonNullable.undefined();

    private JsonNullable<String> content = JsonNullable.undefined();

    private JsonNullable<Integer> index = JsonNullable.undefined();

    private JsonNullable<String> status = JsonNullable.undefined();  // slug статуса

    private JsonNullable<Long> assignee_id = JsonNullable.undefined();
}