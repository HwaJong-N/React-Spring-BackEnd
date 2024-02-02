package ghkwhd.apiServer.member.repository;

import ghkwhd.apiServer.member.domain.Member;
import ghkwhd.apiServer.member.domain.MemberRole;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void saveMember() {
        for (int i = 1; i <= 9; i++) {
            Member member = Member.builder()
                    .email("member" + String.valueOf(i) + "@email.com")
                    .password(passwordEncoder.encode(String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + String.valueOf(i)))
                    .nickname("member" + String.valueOf(i))
                    .needModifyFlag(false)
                    .build();
            member.addRole(MemberRole.USER);

            if (i >= 5) {
                member.addRole(MemberRole.MANAGER);
            }

            if (i >= 8) {
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        }
    }


    @Test
    void readTest() {
        String email = "member9@email.com";
        Member memberWithRoles = memberRepository.findMemberWithRoles(email).orElseThrow();

        log.info("userWithRoles = {}", memberWithRoles);
        log.info("userWithRoles Roles = {}", memberWithRoles.getMemberRoleList());
    }
}