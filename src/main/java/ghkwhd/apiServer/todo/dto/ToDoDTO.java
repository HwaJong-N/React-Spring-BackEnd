package ghkwhd.apiServer.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDTO {
    private Long toDoNo;
    private String title;
    private String content;
    private boolean complete;
    private LocalDate dueDate;
}
