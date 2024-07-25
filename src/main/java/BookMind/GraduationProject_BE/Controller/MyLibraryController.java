package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyLibraryController {

    // 내 서재 _ 사용자 닉네임 불러오기
    @GetMapping("/user-nickname")
    public ResponseEntity<?> getNickname(HttpSession session) {
        System.out.println("Session ID: " + session.getId());
        // 세션에서 사용자 정보를 가져옴
        String loginNickname = (String) session.getAttribute("loginNickname");
        System.out.println(loginNickname + "님의 서재");

        // 로그인 상태 확인 _ 세션에 등록된 사용자가 아닌 경우
        if (loginNickname == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 된 사용자가 아닙니다.");
        }

        // 닉네임을 정상적으로 가져온 경우
        return ResponseEntity.ok(loginNickname);
    }
}
