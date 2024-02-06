package ghkwhd.apiServer.cart.repository;

import ghkwhd.apiServer.cart.dto.CartItemListDTO;
import ghkwhd.apiServer.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /**
     * 특정 사용자의 모든 CartItem 을 가져올 경우
     * @param : email
     * @return : CartItemListDTO
     */
    @Query("select new ghkwhd.apiServer.cart.dto.CartItemListDTO(ci.cartItemNo, ci.quantity, i.itemId, i.itemName, i.price, ii.fileName) " +
            "from CartItem ci " +
            "inner join Cart c on ci.cart = c " +
            "left join Item i on ci.item = i " +
            "left join i.imageList ii " +
            "where c.owner.email = :email " +
            "and (ii.imageOrder IS NULL OR ii.imageOrder = 0) " +
            "order by ci.cartItemNo desc")
    List<CartItemListDTO> getAllCartItemsByEmail(@Param("email") String email);


    /**
     * CartItem 을 추가하는 경우, 이미 CartItem 이 존재하는지 여부 확인
     * @param : email, itemId
     * @return : CartItem
     */
    @Query("select ci from CartItem ci left join Cart c on ci.cart = c where c.owner.email = :email and ci.item.itemId = :itemId")
    Optional<CartItem> getItemByEmailAndItemId(@Param("email") String email, @Param("itemId") Long itemId);


    /**
     * cartItemNo 을 통해 cartNo 를 가져오는 경우 ( CartItem 을 삭제 후, Cart 에 담긴 모든 CartItem 출력을 위해 )
     * @param : cartItemNo
     * @return : cartNo
     */
    @Query("select c.cartNo from Cart c left join CartItem ci on ci.cart = c where ci.cartItemNo = :cartItemNo")
    Long getCartNoByCartItemNo(@Param("cartItemNo") Long cartItemNo);


    /**
     * cartNo 로 모든 CartItem 을 조회하는 경우
     * @param : cartNo
     * @return : All CartItem
     */
    @Query("select new ghkwhd.apiServer.cart.dto.CartItemListDTO(ci.cartItemNo, ci.quantity, i.itemId, i.itemName, i.price, ii.fileName) " +
            "from CartItem ci " +
            "inner join Cart c on ci.cart = c " +
            "left join Item i on ci.item = i " +
            "left join i.imageList ii " +
            "where c.cartNo = :cartNo " +
            "and (ii.imageOrder IS NULL OR ii.imageOrder = 0) " +
            "order by ci.cartItemNo desc")
    List<CartItemListDTO> getAllCartItemsByCartNo(@Param("cartNo") Long cartNo);
}
