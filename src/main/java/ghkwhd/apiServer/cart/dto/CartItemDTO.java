package ghkwhd.apiServer.cart.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private String email;
    private Long itemId;
    private int quantity;
    private Long cartItemNo;
}
