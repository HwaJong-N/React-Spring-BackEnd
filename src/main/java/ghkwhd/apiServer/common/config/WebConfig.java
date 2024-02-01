package ghkwhd.apiServer.common.config;

import ghkwhd.apiServer.common.formatter.LocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormatter());
    }

    /* SecurityConfig 로 이동
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // CORS 를 적용할 URL 정의
                .maxAge(500)
                .allowedOrigins("*")    // 어떤 출처를 허용할 것인지
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"); // 어떤 Http Method 를 허용할 것인지

    }
    */
}
