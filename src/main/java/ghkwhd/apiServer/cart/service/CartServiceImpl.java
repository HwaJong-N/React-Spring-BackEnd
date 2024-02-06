package ghkwhd.apiServer.cart.service;

import ghkwhd.apiServer.cart.dto.CartItemDTO;
import ghkwhd.apiServer.cart.dto.CartItemListDTO;
import ghkwhd.apiServer.cart.entity.Cart;
import ghkwhd.apiServer.cart.entity.CartItem;
import ghkwhd.apiServer.cart.repository.CartItemRepository;
import ghkwhd.apiServer.cart.repository.CartRepository;
import ghkwhd.apiServer.item.entity.Item;
import ghkwhd.apiServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {
        log.info("################### CartServiceImpl ####################");
        String email = cartItemDTO.getEmail();
        Long itemId = cartItemDTO.getItemId();
        int quantity = cartItemDTO.getQuantity();
        Long cartItemNo = cartItemDTO.getCartItemNo();

        if (cartItemNo != null) {
            // 우측 사이드바를 클릭( 사이드바에는 cartItemNo 가 존재 )
            log.info("CartItemNo != NULL");
            CartItem cartItem = cartItemRepository.findById(cartItemNo).orElseThrow();
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            // ReadPage 에서 Add Cart 를 클릭 ( ReadPage 에는 CartItemNo 가 없음 )
            log.info("CartItemNo = NULL");
            Cart cart = getCart(email);
            CartItem cartItem = null;
            Optional<CartItem> itemByEmailAndItemId = cartItemRepository.getItemByEmailAndItemId(email, itemId);
            if (itemByEmailAndItemId.isPresent()) {
                log.info("CartItem is present");    // 장바구니에 존재하는 상품을 추가
                cartItem = itemByEmailAndItemId.get();
                cartItem.setQuantity(quantity);
            } else {
                log.info("CartItem is not present");    // 장바구니에 존재하지 않는 상품을 추가
                Item item = Item.builder().itemId(itemId).build();
                cartItem = CartItem.builder().item(item).cart(cart).quantity(quantity).build();
            }
            cartItemRepository.save(cartItem);
        }
        return getCartItems(email);
    }

    private Cart getCart(String email) {
        Cart cart = null;
        Optional<Cart> cartByEmail = cartRepository.getCartByEmail(email);
        if (cartByEmail.isPresent()) {
            cart = cartByEmail.get();
        } else {
            log.info("해당 email 로 조회된 장바구니가 없음");
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        }
        return cart;
    }

    @Override
    public List<CartItemListDTO> getCartItems(String email) {
        return cartItemRepository.getAllCartItemsByEmail(email);
    }

    @Override
    public List<CartItemListDTO> deleteCartItem(Long cartItemNo) {
        Long cartNo = cartItemRepository.getCartNoByCartItemNo(cartItemNo);
        cartItemRepository.deleteById(cartItemNo);
        return cartItemRepository.getAllCartItemsByCartNo(cartNo);
    }
}
