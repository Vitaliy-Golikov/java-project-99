package hexlet.code.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class UserUpdateDTO {
    private Optional<String> firstName = Optional.empty();
    private Optional<String> lastName = Optional.empty();

    @Email
    private Optional<String> email = Optional.empty();

    @Size(min = 3)
    private Optional<String> password = Optional.empty();
}