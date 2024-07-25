package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.MemberDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    // 마이페이지 _ 사용자 정보 불러오기(가입정보)
    @GetMapping("/user-data")
    public ResponseEntity<?> myPageUserData(HttpSession session) {
        System.out.println("Session ID: " + session.getId());
        MemberDTO userData = new MemberDTO();
        userData.setNickname((String) session.getAttribute("loginNickname"));
        userData.setEmail((String) session.getAttribute("loginEmail"));
        userData.setAge((String) session.getAttribute("userAge"));
        userData.setGender((String) session.getAttribute("userGender"));
        return ResponseEntity.ok(userData);
    }

    // 마이페이지 _ 사용자의 도서 취향 불러오기(개인설정 정보)
    @GetMapping("/user-taste")
    public ResponseEntity<List<String>> myPageUserTaste(HttpSession session) {
        System.out.println("Session ID: " + session.getId());
        List<String> userTaste = (List<String>) session.getAttribute("userInterests");
        System.out.println("userTaste = " + userTaste);

        if (userTaste == null || userTaste.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }

        return ResponseEntity.ok(userTaste); // 200 OK
    }


//    // 회원정보 수정
//    @PostMapping("/update-userData")
//    public
}
