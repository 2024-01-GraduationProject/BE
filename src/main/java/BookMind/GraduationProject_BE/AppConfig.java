package BookMind.GraduationProject_BE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    // RestTemplate를 빈으로 등록해서 애플리케이션에서 REST API 호출 시 사용
    // RestTemplate을 이용해서 Flask의 특정 엔드포인트에 데이터를 전송 가능
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}