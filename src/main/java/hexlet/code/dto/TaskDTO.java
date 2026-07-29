package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private int index;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private String title;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String content;

    private String status;
    private String createdAt;

    @JsonProperty("taskLabelIds")
    private ArrayList<Long> taskLabelIds;
}
