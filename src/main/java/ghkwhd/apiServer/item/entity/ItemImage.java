package ghkwhd.apiServer.item.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemImage {
    private String fileName;
    private int imageOrder;

    public void setOrder(int order) {
        this.imageOrder = order;
    }
}
