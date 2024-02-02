package ghkwhd.apiServer.security.service;

import ghkwhd.apiServer.member.domain.Member;
import ghkwhd.apiServer.member.dto.MemberDTO;
import ghkwhd.apiServer.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 반환형이 UserDetails => UserDetails 를 구현한 클래스인 User 를 상속받은 MemberDTO 가 반환된다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("################## UserDetailsService ##################");
        log.info("################## loadUserByUsername ##################");
        log.info("username = {}", username);

        Member member = memberRepository.findMemberWithRoles(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다"));

        log.info("member = {}", member);
        log.info("member's role = {}", member.getMemberRoleList());

       return new MemberDTO(
               member.getEmail(),
               member.getPassword(),
               member.getNickname(),
               member.isNeedModifyFlag(),
               member.getMemberRoleList().stream()
                       .map(Enum::name).collect(Collectors.toList()));
    }
}
