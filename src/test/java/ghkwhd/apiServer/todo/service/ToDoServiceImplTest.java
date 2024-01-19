package ghkwhd.apiServer.todo.service;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.todo.dto.ToDoDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ToDoServiceImplTest {
    @Autowired
    ToDoService toDoService;

    @Test
    @DisplayName("서비스 저장 테스트")
    void saveTest() {
        ToDoDTO dto = ToDoDTO.builder()
                .title("제목")
                .content("내용")
                .dueDate(LocalDate.of(2024, 01, 17))
                .build();
        Long savedNo = toDoService.save(dto);

        ToDoDTO findDTO = toDoService.get(savedNo);

        log.info("findDTO = {}", findDTO);

        assertThat(dto.getTitle()).isEqualTo(findDTO.getTitle());
        assertThat(dto.getContent()).isEqualTo(findDTO.getContent());
        assertThat(dto.getDueDate()).isEqualTo(findDTO.getDueDate());
    }

    @Test
    @DisplayName("리스트 테스트")
    void getListTest() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(11).build();
        log.info("pageRequestDTO = {}", toDoService.getPageList(pageRequestDTO));
    }
}