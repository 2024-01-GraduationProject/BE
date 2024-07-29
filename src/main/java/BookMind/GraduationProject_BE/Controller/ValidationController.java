package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.EmailRequest;
import BookMind.GraduationProject_BE.DTO.NicknameRequest;
import BookMind.GraduationProject_BE.DTO.PasswordRequest;
import BookMind.GraduationProject_BE.Service.ValidationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    // 닉네임 중복 여부 확인 엔드포인트
    @PostMapping("/validate-nickname")
    public ResponseEntity<Map<String, Boolean>> validateNickname(@RequestBody NicknameRequest nicknameRequest) {
        boolean isDuplicate = validationService.isDuplicateNickname(nicknameRequest.getNickname());
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

    // 마이페이지 _ 회원정보 수정 페이지
    // 변경 전(현재) 비밀번호 확인
    @PostMapping("check-password")
    public ResponseEntity<?> checkingPassword(HttpSession session, @RequestBody PasswordRequest passwordRequest) {
        String inputPassword = passwordRequest.getCurrentPassword();
        // 1. 현재 사용자의 비밀번호 정보를 받아옴
        String userPassword = (String) session.getAttribute("loginPassword");

        // 2. 사용자가 입력한 비밀번호와 현재 사용자의 비밀번호의 일치 여부를 확인
        if (inputPassword.equals(userPassword)) {
            return ResponseEntity.ok("비밀번호가 일치합니다.");
        } else {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }
    }
}
