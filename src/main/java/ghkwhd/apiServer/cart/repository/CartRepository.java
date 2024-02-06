package ghkwhd.apiServer.cart.repository;

import ghkwhd.apiServer.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * Cart 가 있으면 CartItem 만 추가
     * Cart 가 없으면 Cart 도 추가
     */
    @Query("select c from Cart c where c.owner.email = :email")
    Optional<Cart> getCartByEmail(@Param("email") String email);
}
