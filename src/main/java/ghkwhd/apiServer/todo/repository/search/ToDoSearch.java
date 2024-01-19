package ghkwhd.apiServer.todo.repository.search;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.todo.entity.ToDo;
import org.springframework.data.domain.Page;

public interface ToDoSearch {
    Page<ToDo> search(PageRequestDTO requestDTO);
}
