package ghkwhd.apiServer.oauth2.controller;

import ghkwhd.apiServer.jwt.constant.JwtConstant;
import ghkwhd.apiServer.jwt.util.JwtUtils;
import ghkwhd.apiServer.member.dto.MemberDTO;
import ghkwhd.apiServer.member.dto.MemberModifyDTO;
import ghkwhd.apiServer.oauth2.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oauth2Service;

    @GetMapping("/api/member/kakao")
    // React 에서 Access Token 을 받은 후 호출되는 메서드
    public Map<String, Object> getMemberFromKakao(String accessToken) {
        log.info("################### OAuth2Controller ####################");
        log.info("access Token = {}", accessToken);
        MemberDTO memberDTO = oauth2Service.getMember(accessToken);

        Map<String, Object> claims = memberDTO.getClaims();
        claims.put("accessToken", JwtUtils.generateToken(claims, JwtConstant.ACCESS_EXP_TIME));
        claims.put("refreshToken", JwtUtils.generateToken(claims, JwtConstant.REFRESH_EXP_TIME));
        /*
        Spring MVC에서 Map을 반환하는 경우, 프론트엔드에서는 JSON 형태로 된 응답을 받게 됩니다.
        Spring MVC는 기본적으로 Jackson 라이브러리를 사용하여 객체를 JSON 형태로 변환하여 응답합니다.
         */
        return claims;
    }

    @PutMapping("/api/member/modify")
    public Map<String, String> modifySocialMember(@RequestBody MemberModifyDTO memberModifyDTO) {
        log.info("################### OAuth2Controller ####################");
        log.info("MemberModifyDTO = {}", memberModifyDTO);
        oauth2Service.modifyMember(memberModifyDTO);
        return Map.of("result", "modify success");
    }

}
