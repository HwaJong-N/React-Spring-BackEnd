package ghkwhd.apiServer.item.repository;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.item.entity.Item;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void saveTest() {
        /*
        Item item = Item.builder().itemName("색연필").itemDesc("상품 설명").price(25000).build();
        item.addImageString(UUID.randomUUID() + "_" + "Image1.jpg");
        item.addImageString(UUID.randomUUID() + "_" + "Image2.jpg");

        itemRepository.save(item);
        */
        for (int i = 11; i <= 30; i++) {
            Item item = Item.builder().itemName("Test Item " + String.valueOf(i)).itemDesc("상품 설명 " + String.valueOf(i)).price(i * 1500).build();
            item.addImageString(UUID.randomUUID() + "_" + "Item " + String.valueOf(i) + "Image1.jpg");
            item.addImageString(UUID.randomUUID() + "_" + "Item " + String.valueOf(i) + "Image2.jpg");

            itemRepository.save(item);
        }
    }


    @Test
    @Transactional
    void readTest() {
        Long id = 3L;
        Item item = itemRepository.findById(id).orElseThrow();
        // @Transactional 이 없는 경우
        // Item item = itemRepository.selectItemById(id).orElseThrow();
        log.info("item = {}", item);
        log.info("item.getImageList() = {}", item.getImageList());
    }

    @Test
    @Commit
    @Transactional
    void deleteTest() {
        Long id = 3L;
        itemRepository.updateToDelete(id, false);
    }


    @Test
    void updateTest() {
        Long id = 3L;
        Item item = itemRepository.selectItemById(id).orElseThrow();
        item.setPrice(20000);
        item.cleanItemImage();
        item.addImageString(UUID.randomUUID() + "_" + "TestImage1.jpg");
        item.addImageString(UUID.randomUUID() + "_" + "TestImage2.jpg");
        item.addImageString(UUID.randomUUID() + "_" + "TestImage3.jpg");
        itemRepository.save(item);
    }

    @Test
    @DisplayName("목록 조회")
    void listSelectTest() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("itemId").descending());
        Page<Object[]> result = itemRepository.selectList(pageable);
        result.getContent().forEach(arr -> {
            log.info(Arrays.toString(arr));
        });
    }

    @Test
    @DisplayName("QueryDSL 을 사용한 목록 조회")
    void searchTest() {
        PageRequestDTO requestDTO = PageRequestDTO.builder().build();
        //itemRepository.searchList(requestDTO);
    }

}