package ghkwhd.apiServer.cart.entity;

import ghkwhd.apiServer.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "owner")
@Table(indexes = { @Index(name="idx_cart_email", columnList = "cart_owner")})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartNo;

    @OneToOne
    @JoinColumn(name = "cart_owner")   // 외래키 컬럼명
    private Member owner;
}
