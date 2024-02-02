package ghkwhd.apiServer.oauth2.service;

import ghkwhd.apiServer.jwt.constant.JwtConstant;
import ghkwhd.apiServer.member.domain.Member;
import ghkwhd.apiServer.member.domain.MemberRole;
import ghkwhd.apiServer.member.dto.MemberDTO;
import ghkwhd.apiServer.member.dto.MemberModifyDTO;
import ghkwhd.apiServer.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDTO getMember(String accessToken) {
        // 1. accessToken 을 이용해서 사용자 정보를 추출
        String[] memberInfoFromKakao = getMemberInfoFromKakao(accessToken);

        String nickname = memberInfoFromKakao[0];
        String email = memberInfoFromKakao[1];


        Optional<Member> memberWithRoles = memberRepository.findMemberWithRoles(email);
        // 2-1. DB 에 회원 정보가 있는 경우
        if (memberWithRoles.isPresent()) {
            log.info("memberWithRoles = {}", memberWithRoles);
            return entityToDTO(memberWithRoles.get());
        }
        
        // 2-2. DB 에 회원 정보가 없는 경우
        Member newSocialMember = createNewSocialMember(nickname, email);
        memberRepository.save(newSocialMember);
        return entityToDTO(newSocialMember);
    }

    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {
        Member member = memberRepository.findMemberWithRoles(memberModifyDTO.getEmail()).orElseThrow();

        member.setNickname(memberModifyDTO.getNickname());
        member.setPassword(passwordEncoder.encode(memberModifyDTO.getPassword()));
        member.setNeedModifyFlag(false);    // 수정이 끝났으니 변경할 필요 없음으로 변경

        memberRepository.save(member);
    }

    private String[] getMemberInfoFromKakao(String accessToken) {
        log.info("################### OAuth2ServiceImpl ####################");
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", JwtConstant.JWT_TYPE + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(userInfoUri).build();

        // 요청한 결과로 LinkedHashMap 으로 나온다
        ResponseEntity<LinkedHashMap> responseEntity = restTemplate.exchange(
                uriComponentsBuilder.toUri(),
                HttpMethod.GET,
                entity,
                LinkedHashMap.class);

        LinkedHashMap<String, LinkedHashMap> responseEntityBody = responseEntity.getBody();
        LinkedHashMap<String, String> kakaoAccount = responseEntityBody.get("kakao_account");
        LinkedHashMap<String, String> properties = responseEntityBody.get("properties");
        String nickname = properties.get("nickname");
        String email = kakaoAccount.get("email");

        log.info("responseEntity = {}", responseEntity);
        log.info("responseEntityBody = {}", responseEntityBody);
        log.info("kakaoAccount = {}", kakaoAccount);
        log.info("nickname = {}", nickname);
        log.info("email = {}", email);

        return new String[]{nickname, email};
    }


    private String createTempPassword() {
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= 15; i++) {
            sb.append((char) ((int) (Math.random() * 55) + 65));
        }
        return sb.toString();
    }

    private Member createNewSocialMember(String nickname, String email) {
        String tempPassword = createTempPassword();
        log.info("tempPassword = {}", tempPassword);
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .needModifyFlag(true)
                .build();
        member.addRole(MemberRole.USER);
        return member;
    }
}
