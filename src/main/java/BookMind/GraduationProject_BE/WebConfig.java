package BookMind.GraduationProject_BE;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    String ipAddress = "localhost";
    String frontEndPort = "3000";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/test/ip")
                .allowCredentials(true)
                .allowedOrigins("http://"+this.ipAddress+":"+this.frontEndPort);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  // 모든 경로에 대해 CORS 허용
//                .allowCredentials(true)
//                .allowedOrigins("http://"+this.ipAddress+":"+this.frontEndPort)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");  // 허용된 HTTP 메소드
//    }
}