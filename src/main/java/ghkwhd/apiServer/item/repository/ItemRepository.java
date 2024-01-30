package ghkwhd.apiServer.item.repository;

import ghkwhd.apiServer.item.entity.Item;
import ghkwhd.apiServer.item.search.ItemSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @EntityGraph(attributePaths = "imageList")
    @Query("select i from Item i where i.itemId = :itemId")
    Optional<Item> selectItemById(@Param("itemId") Long itemId);

    @Modifying
    @Query("update Item i set i.delFlag = :delFlag where i.itemId = :itemId")
    void updateToDelete(@Param("itemId") Long itemId,
                        @Param("delFlag") boolean delFlag);


    /**
     * 이미지에서 순서가 0인 것들만 select ( 상품의 대표 이미지 )
     * Item 과 ItemImage 는 따로 존재하기 때문에 둘 다 select 에 넣어준다
     * 아래 쿼리의 결과는 Object 라는 배열 안에 각각 Item, ItemImage 가 들어간다
     */
    @Query("select i, ii from Item i left join i.imageList ii where (ii IS NULL OR ii.imageOrder = 0) and i.delFlag = false")
    Page<Object[]> selectList(Pageable pageable);
}
