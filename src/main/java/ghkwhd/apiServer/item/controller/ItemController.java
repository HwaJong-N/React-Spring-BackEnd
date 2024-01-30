package ghkwhd.apiServer.item.controller;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.common.page.PageResponseDTO;
import ghkwhd.apiServer.common.util.FileUtils;
import ghkwhd.apiServer.item.dto.ItemDTO;
import ghkwhd.apiServer.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {
    private final FileUtils fileUtils;
    private final ItemService itemService;

    @PostMapping("/add")
    public Map<String, Long> save(ItemDTO dto) {
        log.info("#####################  ItemController  #####################");
        log.info("Item DTO = {}", dto);

        List<MultipartFile> files = dto.getFiles();

        // 원본 파일을 업로드 하는 파일의 이름으로 문자열을 생성
        // DB 에 저장하기 위한 과정
        List<String> uploadedNames = fileUtils.saveFiles(files);

        dto.setUploadedFileNames(uploadedNames);

        log.info("uploaded file Names = {}", uploadedNames);
        Long savedId = itemService.save(dto);

        return Map.of("RESULT", savedId);
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName) {
        return fileUtils.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ItemDTO> getList(PageRequestDTO requestDTO) {
        return itemService.getList(requestDTO);
    }

    @GetMapping("/{itemId}")
    public ItemDTO getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @PutMapping("/{itemId}")
    public Map<String, String> modifyItem(@PathVariable Long itemId, ItemDTO dto) {
        log.info("################### ItemController modifyItem ####################");
        log.info("ItemDTO = {}", dto);
        dto.setItemId(itemId);
        ItemDTO preItem = itemService.getItem(itemId);
        log.info("preItemDTO = {}", preItem);

        // 새로운 파일 업로드
        List<MultipartFile> files = dto.getFiles();
        List<String> currentUploadFileName = fileUtils.saveFiles(files);
        List<String> uploadedFileNames = dto.getUploadedFileNames();

        log.info("새로 저장된 파일명 : currentUploadFileName = {}", currentUploadFileName); // 새로 저장한 파일명
        log.info("DTO 가 가진 업로디드 파일명 : uploadedFileNames = {}", uploadedFileNames); // 유지하고자 하는 파일의 저장명

        if (currentUploadFileName != null && currentUploadFileName.size() != 0) {
            uploadedFileNames.addAll(currentUploadFileName);
        }
        log.info("service 호출 전 ItemDTO = {}", dto);
        itemService.modify(dto);

        // 기존 파일 삭제
        List<String> preSavedFileNames = preItem.getUploadedFileNames();    // 기존에 저장되어 있던 파일명

        if (preSavedFileNames != null && preSavedFileNames.size() > 0) {
            // 제거해야하는 파일명
            List<String> removeFileName = preSavedFileNames.stream().filter(fileName -> !uploadedFileNames.contains(fileName)).toList();
            fileUtils.deleteFiles(removeFileName);
        }

        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{itemId}")
    public Map<String, String> deleteItem(@PathVariable Long itemId) {
        List<String> deleteFileNames = itemService.getItem(itemId).getUploadedFileNames();
        itemService.remove(itemId);
        fileUtils.deleteFiles(deleteFileNames);
        return Map.of("RESULT", "SUCCESS");
    }
}
