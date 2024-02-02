package ghkwhd.apiServer.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberRoleList")   // member 를 출력했을 때 memberRoleList 는 출력되지 않도록
public class Member {
    @Id
    private String email;
    private String password;
    private String nickname;
    private boolean needModifyFlag; // 소셜 로그인 시, 비밀번호 변경이 필요하기 때문에 socialFlag 에서 변경됨

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();    // 다른 객체로 변경하면 안됨

    public void addRole(MemberRole role) {
        memberRoleList.add(role);
    }

    public void clearRole() {
        memberRoleList.clear();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setNeedModifyFlag(boolean needModifyFlag) {
        this.needModifyFlag = needModifyFlag;
    }
}
