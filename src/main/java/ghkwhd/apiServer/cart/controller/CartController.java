package ghkwhd.apiServer.cart.controller;

import ghkwhd.apiServer.cart.dto.CartItemDTO;
import ghkwhd.apiServer.cart.dto.CartItemListDTO;
import ghkwhd.apiServer.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    
    @PreAuthorize("hasRole('ROLE_USER')")   // 해당 권한을 가지고 있는 사용자만 호출 가능
    @GetMapping("/items")
    public List<CartItemListDTO> getAllCartItems(Principal principal) {
        // Principal 에서 로그인 한 사용자의 정보를 가져온다
        String email = principal.getName(); // Principal 에서 name 이 email 값
        log.info("################### CartController ####################");
        log.info("email = {}", email);
        return cartService.getCartItems(email);
    }

    @PreAuthorize("#cartItemDTO.email == authentication.name")
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart(@RequestBody CartItemDTO cartItemDTO) {
        log.info("################### CartController ####################");
        log.info("CartItemDTO = {}", cartItemDTO);

        if (cartItemDTO.getQuantity() <= 0) {
            log.info("수량에 의한 제거 호출");
            return cartService.deleteCartItem(cartItemDTO.getCartItemNo());
        }
        return cartService.addOrModify(cartItemDTO);
    }

    @PreAuthorize("hasRole('ROLE_UESR')")
    @DeleteMapping("/{cartItemNo}")
    public List<CartItemListDTO> deleteCartItem(@PathVariable Long cartItemNo) {
        return cartService.deleteCartItem(cartItemNo);
    }



}
