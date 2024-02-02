package ghkwhd.apiServer.jwt.filter;

import com.google.gson.Gson;
import ghkwhd.apiServer.jwt.constant.JwtConstant;
import ghkwhd.apiServer.jwt.exception.CustomExpiredJwtException;
import ghkwhd.apiServer.jwt.exception.CustomJwtException;
import ghkwhd.apiServer.jwt.util.JwtUtils;
import ghkwhd.apiServer.member.dto.MemberDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
public class JwtVerifyFilter extends OncePerRequestFilter {

    // 상품 이미지가 보이지 않기에 상품 이미지를 출력하는 /api/items/view 경로를 추가
    private static final String[] whitelist = {"/api/member/**", "/api/refresh", "/api/items/view/**"};

    private static void checkAuthorizationHeader(String header) {
        if(header == null) {
            throw new CustomJwtException("토큰이 전달되지 않았습니다");
        } else if (!header.startsWith(JwtConstant.JWT_TYPE)) {
            throw new CustomJwtException("BEARER 로 시작하지 않는 올바르지 않은 토큰 형식입니다");
        }
    }

    // 필터를 거치지 않을 URL 을 설정하고, true 를 return 하면 현재 필터를 건너뛰고 다음 필터로 이동
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("################## JwtVerifyFilter doFilterInternal ##################");

        String authHeader = request.getHeader(JwtConstant.JWT_HEADER);

        try {
            checkAuthorizationHeader(authHeader);   // header 가 올바른 형식인지 체크
            String token = JwtUtils.getTokenFromHeader(authHeader);
            Map<String, Object> claims = JwtUtils.validateToken(token);

            log.info("authHeader = {}", authHeader);
            log.info("token = {}", token);
            log.info("claims = {}", claims);

            // 토큰 검증이 성공한 경우 추출할 수 있는 사용자 정보들
            String email = (String) claims.get("email");
            String password = (String) claims.get("password");
            String nickname = (String) claims.get("nickname");
            Boolean needModifyFlag = (Boolean) claims.get("needModifyFlag");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            // 사용자의 정보를 통해 새 DTO 객체를 생성
            MemberDTO memberDTO = new MemberDTO(email, password, nickname, needModifyFlag, roleNames);
            log.info("memberDTO = {}", memberDTO);
            log.info("memberDTO.getAuthorities() = {}", memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDTO, password, memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);    // 다음 필터로 이동
        } catch (Exception e) {
            Gson gson = new Gson();
            String json = "";
            if (e instanceof CustomExpiredJwtException) {
                json = gson.toJson(Map.of("Token_Expired", e.getMessage()));
            } else {
                json = gson.toJson(Map.of("error", e.getMessage()));
            }

            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(json);
            printWriter.close();
        }
    }
}
