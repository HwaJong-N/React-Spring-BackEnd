package ghkwhd.apiServer.cart.service;

import ghkwhd.apiServer.cart.dto.CartItemDTO;
import ghkwhd.apiServer.cart.dto.CartItemListDTO;
import jakarta.transaction.Transactional;

import java.util.List;

// 어떤 처리를 하든 다시 장바구니 목록을 보여주기 때문에 반환형은 모두 List<CartItemListDTO>
@Transactional
public interface CartService {
    List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

    // 로그인 시, 사용자의 장바구니를 조회
    List<CartItemListDTO> getCartItems(String email);

    List<CartItemListDTO> deleteCartItem(Long cartItemNo);

}
