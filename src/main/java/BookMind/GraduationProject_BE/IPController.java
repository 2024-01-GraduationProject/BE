package BookMind.GraduationProject_BE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

// 클라이언트의 IP 주소를 반환하는 RESTful API를 정의하는 클래스
@RestController // RESTful 웹 서비스의 컨트롤러임을 의미
@RequestMapping("/test")
public class IPController {

    @PostMapping("/ip")
    public ResponseEntity<String> ip(HttpServletRequest request) {
        // 요청을 보낸 클라이언트의 IP주소를 반환
        return ResponseEntity.ok(request.getRemoteAddr());
    }
}

