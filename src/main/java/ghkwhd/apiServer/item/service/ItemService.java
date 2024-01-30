package ghkwhd.apiServer.item.service;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.common.page.PageResponseDTO;
import ghkwhd.apiServer.item.dto.ItemDTO;
import ghkwhd.apiServer.item.entity.Item;
import ghkwhd.apiServer.item.entity.ItemImage;

import java.util.List;

public interface ItemService {
    PageResponseDTO<ItemDTO> getList(PageRequestDTO requestDTO);
    Long save(ItemDTO itemDTO);

    ItemDTO getItem(Long itemId);

    void modify(ItemDTO itemDTO);

    void remove(Long itemId);

    default Item dtoToEntity(ItemDTO dto) {
        Item item = Item.builder()
                .itemId(dto.getItemId())
                .itemName(dto.getItemName())
                .itemDesc(dto.getItemDesc())
                .price(dto.getPrice())
                .build();

        // 업로드 처리가 끝난 파일들의 이름 리스트 ( FileUtils 에 의해 save 과정을 거침 )
        List<String> uploadedFileNames = dto.getUploadedFileNames();
        if (uploadedFileNames == null || uploadedFileNames.size() == 0) {
            return item;
        }

        uploadedFileNames.forEach(item::addImageString);

        return item;
    }

    default ItemDTO entityToDTO(Item item) {
        ItemDTO itemDTO = ItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemDesc(item.getItemDesc())
                .price(item.getPrice())
                .delFlag(item.isDelFlag())
                .build();

        List<ItemImage> imageList = item.getImageList();
        // Item 에 있는건 ItemImage 인데 ItemDTO 에 필요한 것은 업로드된 파일명 string 이기 때문에 이를 변환하는 작업이 필요
        if (imageList == null || imageList.size() == 0) {
            return itemDTO;
        }
        // 조회할 때는 DB에 저장된 파일명이 Item 에 들어가기 때문에 파일명만 꺼내면 해당 데이터가 업로드된 파일명
        List<String> dbFileNameList = imageList.stream().map(ItemImage::getFileName).toList();
        itemDTO.setUploadedFileNames(dbFileNameList);

        return itemDTO;
    }
}
