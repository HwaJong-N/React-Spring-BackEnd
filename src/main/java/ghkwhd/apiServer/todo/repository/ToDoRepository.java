package ghkwhd.apiServer.todo.repository;

import ghkwhd.apiServer.todo.entity.ToDo;
import ghkwhd.apiServer.todo.repository.search.ToDoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long>, ToDoSearch {

}
