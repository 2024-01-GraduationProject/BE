package BookMind.GraduationProject_BE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class IPController {

    @PostMapping("/ip")
    public ResponseEntity<String> ip(HttpServletRequest request) {
        // 요청을 보낸 클라이언트의 IP주소를 반환
        return ResponseEntity.ok(request.getRemoteAddr());
    }
}

