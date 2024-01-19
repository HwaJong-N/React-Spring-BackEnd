package ghkwhd.apiServer.todo.repository;

import ghkwhd.apiServer.todo.entity.ToDo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ToDoRepositoryTest {
    @Autowired
    ToDoRepository toDoRepository;

    @Test
    @DisplayName("repositoty 객체 확인")
    void checkNotNull() {
        // toDoRepository Not Null 확인
        Assertions.assertNotNull(toDoRepository);
    }

    @Test
    @DisplayName("ToDo 저장 테스트")
    void insertTest() {
        ToDo toDo = ToDo.builder()
                .title("제목")
                .content("내용")
                .dueDate(LocalDate.of(2024, 01, 17))
                .build();

        ToDo savedToDo = toDoRepository.save(toDo);
        Long toDoNo = savedToDo.getToDoNo();

        ToDo findToDo = toDoRepository.findById(toDoNo).orElseThrow();

        assertThat(findToDo).isEqualTo(savedToDo);
    }

    @Test
    @DisplayName("ToDo 페이징 테스트")
    void pagingTest() {
        for (int i = 0; i < 21; i++) {
            String str = "내용" + String.valueOf(i + 1);
            ToDo toDo = ToDo.builder()
                    .title("제목")
                    .content(str)
                    .dueDate(LocalDate.of(2024, 01, 17))
                    .build();
            ToDo savedToDo = toDoRepository.save(toDo);
        }

        // 페이지 번호는 0부터 시작
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("toDoNo").descending());
        Page<ToDo> all = toDoRepository.findAll(pageable);

        System.out.println("total pages = " + all.getTotalPages());
        List<ToDo> todoList = all.getContent();

        for (ToDo todo : todoList) {
            System.out.print("no = " + todo.getToDoNo());
            System.out.println(", content = " + todo.getContent());
        }
    }
}
