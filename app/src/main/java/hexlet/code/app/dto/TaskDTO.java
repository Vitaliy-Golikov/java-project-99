package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Integer index;
    private String title;
    private String content;
    private String status;  // slug статуса
    private Long assignee_id;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}