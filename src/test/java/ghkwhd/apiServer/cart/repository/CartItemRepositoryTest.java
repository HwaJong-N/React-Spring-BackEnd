package ghkwhd.apiServer.cart.repository;

import ghkwhd.apiServer.cart.dto.CartItemListDTO;
import ghkwhd.apiServer.cart.entity.Cart;
import ghkwhd.apiServer.cart.entity.CartItem;
import ghkwhd.apiServer.item.entity.Item;
import ghkwhd.apiServer.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@SpringBootTest
class CartItemRepositoryTest {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    void getListByEmail() {
        String email = "member1@email.com";

        List<CartItemListDTO> allCartItemsByEmail = cartItemRepository.getAllCartItemsByEmail(email);

        for (CartItemListDTO cartItemListDTO : allCartItemsByEmail) {
            log.info("Cart Item = {}", cartItemListDTO);
        }
    }

    @Test
    @Commit
    void saveCartItem() {
        // 테스트를 위한 데이터 INSERT
        String email = "member1@email.com";
        //Long itemId = 76L;
        Long itemId = 56L;
        int quantity = 1;

        // 1. 해당 사용자의 장바구니에 해당 상품이 있는지 확인
        CartItem cartItem = null;
        Optional<CartItem> itemByEmailAndItemId = cartItemRepository.getItemByEmailAndItemId(email, itemId);
        // 1-1. 장바구니에 해당 상품이 존재하는 경우
        if (itemByEmailAndItemId.isPresent()) {
            cartItem = itemByEmailAndItemId.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
        // 1-2. 존재하지 않는 경우
            // 장바구니 존재 여부 확인
            Cart cart = null;
            Optional<Cart> cartByEmail = cartRepository.getCartByEmail(email);
            // 1-2-1. 장바구니 자체가 존재하지 않는 경우
            if (cartByEmail.isEmpty()) {
                Member member = Member.builder().email(email).build();
                Cart newCart = Cart.builder().owner(member).build();
                cart = cartRepository.save(newCart);
            } else {
                // 1-2-2. 장바구니가 있지만 해당 상품이 없는 경우
                cart = cartByEmail.get();
            }
            // 이 시점에 장바구니는 존재하게 된다
            // 장바구니에 저장 하려는 상품을 생성 및 장바구니에 추가 ( => CartItem 생성 )
            Item item = Item.builder().itemId(itemId).build();
            cartItem = CartItem.builder().cart(cart).item(item).quantity(quantity).build();
        }
        cartItemRepository.save(cartItem);
    }


    @Test
    @Commit
    void modifyCartItem() {
        Long cartItemNo = 1L;
        int quantity = 4;

        CartItem cartItem = cartItemRepository.findById(cartItemNo).orElseThrow();
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Test
    @Commit
    void deleteCartItemAndGetAllCartItems() {
        // 장바구니의 상품을 삭제하면 해당 장바구니의 모든 아이템들을 다시 가져오도록
        Long cartItemNo = 3L;

        // 1. 삭제할 아이템이 있었던 장바구니 번호를 구한다
        Long cartNo = cartItemRepository.getCartNoByCartItemNo(cartItemNo);

        // 2. 장바구니의 상품을 삭제
        cartItemRepository.deleteById(cartItemNo);

        // 3. 삭제 후, 해당 장바구니에 담긴 모든 상품을 조회
        List<CartItemListDTO> allCartItemsByCartNo = cartItemRepository.getAllCartItemsByCartNo(cartNo);
        for (CartItemListDTO cartItemListDTO : allCartItemsByCartNo) {
            log.info("Cart Item = {}", cartItemListDTO);
        }
    }

}