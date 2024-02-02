package ghkwhd.apiServer.oauth2.service;

import ghkwhd.apiServer.member.domain.Member;
import ghkwhd.apiServer.member.dto.MemberDTO;
import ghkwhd.apiServer.member.dto.MemberModifyDTO;
import jakarta.transaction.Transactional;

@Transactional
public interface OAuth2Service {
    MemberDTO getMember(String accessToken);

    void modifyMember(MemberModifyDTO memberModifyDTO);

    default MemberDTO entityToDTO(Member member) {
        return new MemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.getNickname(),
                member.isNeedModifyFlag(),
                member.getMemberRoleList().stream().map(Enum::name).toList());
    }
}
