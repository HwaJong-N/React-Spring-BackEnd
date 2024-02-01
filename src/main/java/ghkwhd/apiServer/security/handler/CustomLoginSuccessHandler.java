package ghkwhd.apiServer.security.handler;

import com.google.gson.Gson;
import ghkwhd.apiServer.jwt.constant.JwtConstant;
import ghkwhd.apiServer.jwt.util.JwtUtils;
import ghkwhd.apiServer.member.dto.MemberDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("################## LoginSuccessHandler ##################");
        log.info("################## 로그인 성공!!! ##################");
        log.info("authentication = {}", authentication);
        log.info("authentication.getPrincipal() = {}", authentication.getPrincipal());

        MemberDTO principal = (MemberDTO) authentication.getPrincipal();

        Map<String, Object> claims = principal.getClaims(); // Member 의 모든 정보가 담김( 이메일, 비밀번호 등등 )
        // Access Token, Refresh Token 생성
        claims.put("accessToken", JwtUtils.generateToken(claims, JwtConstant.ACCESS_EXP_TIME));
        claims.put("refreshToken", JwtUtils.generateToken(claims, JwtConstant.REFRESH_EXP_TIME));

        Gson gson = new Gson();
        String json = gson.toJson(claims);

        response.setContentType("application/json; charset=UTF-8");

        PrintWriter printWriter = response.getWriter();
        printWriter.println(json);
        printWriter.close();
    }
}
