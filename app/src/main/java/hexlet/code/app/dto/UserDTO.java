package hexlet.code.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    @NotBlank
    @Email
    private String email;

    private LocalDate createdAt;
}
