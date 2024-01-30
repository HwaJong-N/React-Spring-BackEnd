package ghkwhd.apiServer.item.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_item")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageList")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private String itemName;
    private int price;
    private String itemDesc;
    private boolean delFlag;

    @ElementCollection
    @Builder.Default
    private List<ItemImage> imageList = new ArrayList<>();

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

    // 업로드된 파일의 순서를 지정해주는 메서드
    public void addImage(ItemImage itemImage) {
        itemImage.setOrder(imageList.size());
        imageList.add(itemImage);
    }
    
    public void addImageString(String fileName) {
        ItemImage itemImage = ItemImage.builder()
                                        .fileName(fileName)
                                        .build();
        addImage(itemImage);
    }

    public void cleanItemImage() {
        this.imageList.clear();
    }
}
