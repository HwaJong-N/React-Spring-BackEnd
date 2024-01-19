package ghkwhd.apiServer.todo.service;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.common.page.PageResponseDTO;
import ghkwhd.apiServer.todo.dto.ToDoDTO;
import ghkwhd.apiServer.todo.entity.ToDo;
import ghkwhd.apiServer.todo.repository.ToDoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService{

    private final ToDoRepository toDoRepository;

    @Override
    public Long save(ToDoDTO dto) {
        return toDoRepository.save(dtoToEntity(dto)).getToDoNo();
    }

    @Override
    public ToDoDTO get(Long toDoNo) {
        return entityToDTO(toDoRepository.findById(toDoNo).orElseThrow());
    }

    @Override
    public void modify(ToDoDTO dto) {
        ToDo toDo = toDoRepository.findById(dto.getToDoNo()).orElseThrow();
        toDo.setTitle(dto.getTitle());
        toDo.setContent(dto.getContent());
        toDo.setComplete(dto.isComplete());
        toDo.setDueDate(dto.getDueDate());
        toDoRepository.save(toDo);
    }

    @Override
    public void remove(Long toDoNO) {
        toDoRepository.deleteById(toDoNO);
    }

    @Override
    public PageResponseDTO<ToDoDTO> getPageList(PageRequestDTO requestDTO) {
        // ToDo List 가 반환됨
        Page<ToDo> searchResult = toDoRepository.search(requestDTO);
        
        // 반환 받은 것은 ToDo List, 반환해야 하는 것은 ToDoDTO List
        List<ToDoDTO> dtoList = searchResult.get().map(this::entityToDTO).toList();

        log.info("dtoList size = {}", dtoList.size());

        return PageResponseDTO.<ToDoDTO>withAll()
                .dtoList(dtoList)
                .requestDTO(requestDTO)
                .total(searchResult.getTotalElements())
                .build();
    }
}
