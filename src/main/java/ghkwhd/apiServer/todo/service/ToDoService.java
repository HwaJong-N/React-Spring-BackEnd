package ghkwhd.apiServer.todo.service;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.common.page.PageResponseDTO;
import ghkwhd.apiServer.todo.dto.ToDoDTO;
import ghkwhd.apiServer.todo.entity.ToDo;

public interface ToDoService {

    Long save(ToDoDTO dto);
    ToDoDTO get(Long toDoNo);

    void modify(ToDoDTO dto);

    void remove(Long toDoNO);

    PageResponseDTO<ToDoDTO> getPageList(PageRequestDTO requestDTO);

    default ToDoDTO entityToDTO(ToDo entity) {
        return ToDoDTO.builder()
                    .toDoNo(entity.getToDoNo())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .dueDate(entity.getDueDate())
                    .complete(entity.isComplete())
                    .build();
    }

    default ToDo dtoToEntity(ToDoDTO dto) {
        return ToDo.builder()
                .toDoNo(dto.getToDoNo())
                .title(dto.getTitle())
                .content(dto.getContent())
                .dueDate(dto.getDueDate())
                .complete(dto.isComplete())
                .build();
    }
}
