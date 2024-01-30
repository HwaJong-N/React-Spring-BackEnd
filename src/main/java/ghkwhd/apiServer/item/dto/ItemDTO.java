package ghkwhd.apiServer.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    private Long itemId;
    private String itemName;
    private int price;
    private String itemDesc;    // item 설명
    private boolean delFlag;    // 삭제 여부

    // 업로드 해야 하는 파일( 브라우저에서 받은 파일 )
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();


    // 업로드된 파일명( 서버에 저장되어 있는 파일명 )
    @Builder.Default
    private List<String> uploadedFileNames = new ArrayList<>();

}
