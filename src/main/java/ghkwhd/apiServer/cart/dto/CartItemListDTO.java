package ghkwhd.apiServer.cart.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {
    private Long cartItemNo;
    private int quantity;
    private Long itemId;
    private String itemName;
    private int price;
    private String imageFile;

    public CartItemListDTO(Long cartItemNo, int quantity, Long itemId, String itemName, int price, String imageFile) {
        this.cartItemNo = cartItemNo;
        this.quantity = quantity;
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.imageFile = imageFile;
    }
}
