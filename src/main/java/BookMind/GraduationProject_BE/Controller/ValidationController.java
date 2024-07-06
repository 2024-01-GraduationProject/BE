package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.EmailRequest;
import BookMind.GraduationProject_BE.Service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ValidationController {

    @Autowired
    ValidationService validationService;

    // 이메일 중복 여부 확인 엔드포인트
    @PostMapping("/validate-email")
    public ResponseEntity<Map<String, Boolean>> validateEmail(@RequestBody EmailRequest emailRequest) {
        boolean isDuplicate = validationService.isDuplicateEmail(emailRequest.getEmail());
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }
}
