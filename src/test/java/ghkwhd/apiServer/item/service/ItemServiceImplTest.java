package ghkwhd.apiServer.item.service;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.common.page.PageResponseDTO;
import ghkwhd.apiServer.item.dto.ItemDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ItemServiceImplTest {
    @Autowired
    private ItemService itemService;

    @Test
    @DisplayName("서비스 목록 조회 테스트")
    void testGetList() {
        PageRequestDTO requestDTO = PageRequestDTO.builder().build();
        PageResponseDTO<ItemDTO> dtoList = itemService.getList(requestDTO);
        log.info("dtoList = {}", dtoList);
    }

    @Test
    @DisplayName("저장 테스트")
    void saveTest() {
        ItemDTO itemDTO = ItemDTO.builder()
                .itemName("service item test")
                .itemDesc("item service description")
                .price(23000)
                .build();
        log.info("ItemDTO = {}", itemDTO);
        itemDTO.setUploadedFileNames(List.of(
                UUID.randomUUID() + "_" + "Test1.jpg",
                UUID.randomUUID() + "_" + "Test2.jpg")
        );

        itemService.save(itemDTO);
    }
}