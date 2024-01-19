package ghkwhd.apiServer.todo.repository.search;

import com.querydsl.jpa.JPQLQuery;
import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.todo.entity.QToDo;
import ghkwhd.apiServer.todo.entity.ToDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Slf4j
public class ToDoSearchImpl extends QuerydslRepositorySupport implements ToDoSearch {
    /*
    기본 생성자 형태
    public ToDoSearchImpl(Class<?> domainClass) {
        super(domainClass);
    }
    */

    public ToDoSearchImpl() {
        super(ToDo.class);
    }

    @Override
    public Page<ToDo> search(PageRequestDTO requestDTO) {
        // 쿼리 실행을 위한 가상의 객체
        QToDo toDo = QToDo.toDo;

        // 쿼리 생성
        JPQLQuery<ToDo> query = from(toDo);

        // 페이징 처리 적용
        // PageRequest 는 0부터 시작하기 때문에 -1 이 필요
        Pageable pageable = PageRequest.of(requestDTO.getPage() - 1, requestDTO.getSize(), Sort.by("toDoNo").descending());
        this.getQuerydsl().applyPagination(pageable, query);

        // 쿼리 실행 결과 반환
        return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
    }
}
