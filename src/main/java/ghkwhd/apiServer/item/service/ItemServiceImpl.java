package ghkwhd.apiServer.item.service;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.common.page.PageResponseDTO;
import ghkwhd.apiServer.item.dto.ItemDTO;
import ghkwhd.apiServer.item.entity.Item;
import ghkwhd.apiServer.item.entity.ItemImage;
import ghkwhd.apiServer.item.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public PageResponseDTO<ItemDTO> getList(PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(requestDTO.getPage() -1, requestDTO.getSize(), Sort.by("itemId").descending());
        // Object[] 는 { Item, ItemImage } 를 원소로 가진다
        Page<Object[]> objects = itemRepository.selectList(pageable);
        
        // Object 에 담긴 객체들을 끌어와서 정보들을 추출하여 ItemDTO 객체를 생성
        List<ItemDTO> dtoList = objects.get().map(arr -> {
            Item item = (Item) arr[0];
            ItemImage itemImage = (ItemImage) arr[1];
            ItemDTO itemDTO = ItemDTO.builder()
                    .itemId(item.getItemId())
                    .itemName(item.getItemName())
                    .itemDesc(item.getItemDesc())
                    .price(item.getPrice())
                    .build();

            if (itemImage != null) {
                String fileName = itemImage.getFileName();  // selectList 로 조회할 때 대표 이미지만 가져오기 때문에 단일 String
                itemDTO.setUploadedFileNames(List.of(fileName));    // itemDTO 에는 업로드 된 파일명 필드가 존재
            }
            return itemDTO;
        }).toList();

        long totalCount = objects.getTotalElements();

        return PageResponseDTO.<ItemDTO>withAll()
                .dtoList(dtoList)
                .total(totalCount)
                .requestDTO(requestDTO)
                .build();
    }

    @Override
    public Long save(ItemDTO itemDTO) {
        Item item = dtoToEntity(itemDTO);
        log.info("#####################  ItemServiceImpl  #####################");
        log.info("item = {}", item);
        log.info("item imageList = {}", item.getImageList());
        return itemRepository.save(item).getItemId();
    }

    @Override
    public ItemDTO getItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        return entityToDTO(item);
    }

    @Override
    public void modify(ItemDTO itemDTO) {
        Item item = itemRepository.findById(itemDTO.getItemId()).orElseThrow();
        log.info("########## ItemService modify ##############");
        log.info("itemDTO = {}", itemDTO);

        item.setItemName(itemDTO.getItemName());
        item.setPrice(itemDTO.getPrice());
        item.setItemDesc(itemDTO.getItemDesc());
        item.setDelFlag(itemDTO.isDelFlag());

        // 이미지 변경 처리
        List<String> uploadedFileNames = itemDTO.getUploadedFileNames();
        // 전체 초기화 후 다시 저장
        item.cleanItemImage();
        if (uploadedFileNames != null && uploadedFileNames.size() != 0) {
            uploadedFileNames.forEach(item::addImageString);
        }
        log.info("item = {}", item);
        log.info("item.getImageList = {}", item.getImageList());
        itemRepository.save(item);
    }

    @Override
    public void remove(Long itemId) {
        // Item 이 삭제되면 Item 과 연관된 IteImage 도 함께 삭제됨
        itemRepository.deleteById(itemId);
    }
}
