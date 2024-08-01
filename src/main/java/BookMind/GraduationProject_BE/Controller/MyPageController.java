package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.DTO.UpdateMemberDTO;
import BookMind.GraduationProject_BE.Entity.Agreements;
import BookMind.GraduationProject_BE.Entity.Member;
import BookMind.GraduationProject_BE.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    @Autowired
    private final MemberService memberService;

    // 회원정보 불러오기(가입된 정보)
    @GetMapping("/user-data")
    public ResponseEntity<?> myPageUserData(HttpSession session) {
        // 세션이 존재하지 않을 경우
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("세션이 존재하지 않습니다. 로그인해 주세요.");
        }
        // 로그인 상태 확인 (세션은 존재)
        System.out.println("Session ID: " + session.getId());

        // 로그인된 사용자 id 확인
        Long userId = (Long) session.getAttribute("userId");
        System.out.println("userId = " + userId);

        String loginNickname = (String) session.getAttribute("loginNickname");
        if (loginNickname == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("세션이 만료되었거나, 로그인하지 않았습니다.");
        }
        // 정상적으로 로그인한 경우
        MemberDTO userData = new MemberDTO();
        userData.setNickname(loginNickname); // 닉네임
        userData.setEmail((String) session.getAttribute("loginEmail")); // 이메일
        userData.setAge((String) session.getAttribute("userAge")); // 연령대
        userData.setGender((String) session.getAttribute("userGender")); // 성별
        userData.setMood((List<String>) session.getAttribute("userInterests")); // 관심분야
        userData.setLoginMethod((String) session.getAttribute("loginMethod")); // 로그인 방식
        userData.setAgreements((Agreements) session.getAttribute("userAgreements")); // 약관동의

        return ResponseEntity.ok(userData);
    }

    // 회원정보 수정(업데이트)
    @PostMapping("/update-userData")
    public ResponseEntity<?> save(HttpSession session, @RequestBody UpdateMemberDTO updateMemberDTO) {
        System.out.println("변경 요청된 정보:" + updateMemberDTO);

        // 로그인된 사용자 id 확인
        Long userId = (Long) session.getAttribute("userId");
        System.out.println("userId = " + userId);

        // 세션에 로그인 정보가 없을 때
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 없습니다.");
        }

        try {
            Member updatedMember = memberService.updateMember(userId, updateMemberDTO);
            System.out.println("회원정보가 업데이트 되었습니다.");
            System.out.println("updatedMember = " + updatedMember);
            return ResponseEntity.ok(updatedMember);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 저장 도중에 문제가 발생했습니다.");
        }
    }
}
