package ghkwhd.apiServer.common.page;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.IntStream;

// 목록 데이터를 반환
@Slf4j
@Data
public class PageResponseDTO<E> {
    /**
     * numPerPage : 한 페이지 당 게시글 수( PageRequestDTO 의 size )
     * pageSize : 한 번에 몇 개의 페이지를 보여 줄 것인지 ( 여기서는 10개로 지정 )
     */
    private int numPerPage, pageSize = 10;

    // 검색조건은 PageRequestDTO 에 담겨있음
    private PageRequestDTO requestDTO;

    private List<E> dtoList;    // 페이지에 보여지는 DTO 들이 담긴 List
    private List<Integer> pageNumberList;   // 페이지 번호들이 담긴 List

    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, currentPage;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO requestDTO, List<E> dtoList, long total) {
        this.requestDTO = requestDTO;
        this.currentPage = requestDTO.getPage();
        this.numPerPage = requestDTO.getSize();
        this.dtoList = dtoList;
        this.totalCount = (int) total;
        this.totalPage = (int)(Math.ceil(totalCount / (double)requestDTO.getSize()));

        // 시작 페이지 계산
        int start = (((requestDTO.getPage() - 1) / pageSize) * pageSize) + 1;
        /**
         * 끝 페이지 계산
         * 하지만 이 계산 값이 무조건 마지막이라고 할 수 없음 ( 8페이지까지 있을 때 8페이지가 마지막인데 end 값은 10이 나오게 됨 )
         * 그래서 총 페이지 수와 비교해서 작은 값이 마지막 페이지가 된다
         */
        int end = Math.min(start + pageSize - 1, this.totalPage);

        // 이전버튼, 다음버튼
        this.prev = start > 1;
        this.next = end != this.totalPage;

        // 페이지 목록
        this.pageNumberList = IntStream.rangeClosed(start, end).boxed().toList();

        // 이전 페이지, 다음 페이지
        this.prevPage = prev ? start - 1 : 0; // 이전 페이지가 있으면 start - 1
        this.nextPage = next ? end + 1 : 0;
    }
}
