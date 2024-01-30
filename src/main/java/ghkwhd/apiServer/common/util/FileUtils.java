package ghkwhd.apiServer.common.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtils {

    @Value("${file.upload.path}")
    private String uploadPath;

    // 생성된 이후에 호출되는 메서드를 작성
    @PostConstruct
    public void init() {
        log.info("FileUtils init start");
        File tempFolder = new File(uploadPath);
        if (!tempFolder.exists()) {
            log.info("make upload Directory");
            tempFolder.mkdir();
        }
        uploadPath = tempFolder.getAbsolutePath();
        log.info("uploadPath = {}", uploadPath);
    }

    // 업로드 된 파일명 반환
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
        List<String> uploadNames = new ArrayList<>();

        if (files == null || files.size() == 0) {
            return uploadNames;
        }

        for (MultipartFile file : files) {
            // 저장 이름 = 랜덤_원래 파일 이름
            String saveName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path savePath = Paths.get(uploadPath, saveName);    // 저장할 경로와 이름을 지정

            try {
                Files.copy(file.getInputStream(), savePath);    // 원본 파일 저장
                uploadNames.add(saveName);  // 업로드된 파일명 추가

                // 썸네일 생성( 이미지 파일인 경우에만 ), 이미지 사이즈 조정
                if (file.getContentType().startsWith("image")) {
                    Path thumbnailPath = Paths.get(uploadPath, "th_" + saveName);
                    Thumbnails.of(savePath.toFile())
                            .size(200, 200)
                            .toFile(thumbnailPath.toFile());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            
        }

        return uploadNames;
    }


    // 파일 조회
    public ResponseEntity<Resource> getFile(String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        // 이미지가 없는 경우에 디폴트 이미지를 반환
        if (!resource.isReadable()) {
            resource = new FileSystemResource(uploadPath + File.separator + "default.PNG");
        }

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }


    // 파일 삭제
    public void deleteFiles(List<String> fileNames) {

        log.info("################ FileUtils ###############");
        log.info("fileNames = {}", fileNames);

        if (fileNames == null || fileNames.size() == 0) {
            return;
        }

        fileNames.forEach(fileName -> {
            String thumbnailFileName = "th_" + fileName;

            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path originFilePath = Paths.get(uploadPath, fileName);

            // 파일이 존재하는 경우 삭제
            try {
                Files.deleteIfExists(thumbnailPath);
                Files.deleteIfExists(originFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
