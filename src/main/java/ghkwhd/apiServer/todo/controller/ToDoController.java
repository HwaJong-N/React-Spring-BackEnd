package ghkwhd.apiServer.todo.controller;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.common.page.PageResponseDTO;
import ghkwhd.apiServer.todo.dto.ToDoDTO;
import ghkwhd.apiServer.todo.service.ToDoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class ToDoController {

    private final ToDoService toDoService;

    @GetMapping("/{toDoNo}")
    public ToDoDTO getToDO(@PathVariable Long toDoNo) {
        return toDoService.get(toDoNo);
    }

    @GetMapping("/list")
    public PageResponseDTO<ToDoDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO = {}", pageRequestDTO);
        return toDoService.getPageList(pageRequestDTO);
    }

    @PostMapping("/add")
    public Map<String, Long> save(@RequestBody ToDoDTO dto) {
        log.info("ToDoDTO = {}", dto);
        Long savedNo = toDoService.save(dto);
        return Map.of("ToDoNo", savedNo);
    }

    @PutMapping("/{toDoNo}")
    public Map<String, String> modify(@PathVariable Long toDoNo, @RequestBody ToDoDTO dto) {
        log.info("ToDoDTO = {}", dto);
        toDoService.modify(dto);
        return Map.of("result", "success");
    }

    @DeleteMapping("/{toDoNo}")
    public Map<String, String> delete(@PathVariable Long toDoNo) {
        toDoService.remove(toDoNo);
        return Map.of("result", "success");
    }


}
