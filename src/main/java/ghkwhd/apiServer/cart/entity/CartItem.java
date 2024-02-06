package ghkwhd.apiServer.cart.entity;

import ghkwhd.apiServer.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"cart"})
@Table(indexes = {
        @Index(name="idx_cart_item_cart_no", columnList = "cart_no"),
        @Index(name="idx_cart_item_iid_cno", columnList = "item_id, cart_no")
})
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemNo;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name="cart_no")
    private Cart cart;

    private int quantity;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
