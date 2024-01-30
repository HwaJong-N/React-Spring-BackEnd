package ghkwhd.apiServer.item.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.common.page.PageResponseDTO;
import ghkwhd.apiServer.item.dto.ItemDTO;
import ghkwhd.apiServer.item.entity.Item;
import ghkwhd.apiServer.item.entity.QItem;
import ghkwhd.apiServer.item.entity.QItemImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class ItemSearchImpl extends QuerydslRepositorySupport implements ItemSearch {
    public ItemSearchImpl() {
        super(Item.class);
    }

    /**
     * 해당 메서드는 사용하지 않음
     * ElementCollection 을 QueryDSL 에서 어떻게 사용하는지를 구현한 코드
     * 리스트 조회는 ItemRepository 에서 구현한 selectList 를 사용
     */
    @Override
    public PageResponseDTO<ItemDTO> searchList(PageRequestDTO requestDTO) {
        log.info("ItemSearchImpl requestDTO = {}", requestDTO);

        Pageable pageable = PageRequest.of(requestDTO.getPage() - 1, requestDTO.getSize(), Sort.by("itemId").descending());

        QItem item = QItem.item;
        QItemImage itemImage = QItemImage.itemImage;

        JPQLQuery<Item> query = from(item); // from 절까지 JPQL 쿼리가 생성됨 ( select item from Item item )
        query.leftJoin(item.imageList, itemImage);  // @ElementCollection 을 사용할 때 쿼리는 JOIN 을 사용해야함
        log.info("left join query = {}", query);

        query.where(itemImage.imageOrder.eq(0));    // 0번째 이미지만 가져오기
        getQuerydsl().applyPagination(pageable, query);

        List<Item> itemList = query.fetch();
        List<Tuple> itemList2 = query.select(item, itemImage).fetch();
        long itemCount = query.fetchCount();

        log.info("itemList = {}", itemList);
        log.info("itemList2 = {}", itemList2);

        return null;
    }
}
