package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.InformationAndTasteDTO;
import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.Entity.Member;
import BookMind.GraduationProject_BE.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class MemberController {

    // 로그를 기록하기 위한 로그 객체 생성
    //private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private final MemberService memberService;

    // 회원가입 폼 제출(Post 요청) (회원 데이터를 등록)
    @PostMapping("/register")
    public ResponseEntity<?> save(@RequestBody MemberDTO memberDTO) {
        System.out.println("Received register request " + memberDTO);
        try {
            // MemberDTO를 Member로 변환하는 로직은 서비스 계층에서 이루어짐
            // 그렇게 되면 컨트롤러는 순수하게 HTTP 요청과 응답만 처리 가능
            memberService.join(memberDTO); // 서비스 계층의 join 메서드 호출하여 회원가입 처리
            System.out.println("memberController.save");
            System.out.println("memberDTO = " + memberDTO);
            return ResponseEntity.ok("회원 정보가 성공적으로 저장되었습니다.");
        } catch (IllegalStateException e) { // 중복 회원 예외가 발생
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) { // 그 외 예외 발생
            return ResponseEntity.status(500).body("회원 정보 저장 도중에 문제가 발생했습니다.");
        }
    }

    // 사용자의 연령, 성별, 취향 정보 등록
    @PostMapping("/save-taste")
    public ResponseEntity<Member> updateMemberTaste(@RequestBody InformationAndTasteDTO informationAndTasteDTO) {
        try {
            System.out.println("informationAndTasteDTO = " + informationAndTasteDTO);
            Member updatedMember = memberService.saveTaste(informationAndTasteDTO);
            System.out.println("updatedMember = " + updatedMember);
            // 응답 상태 코드: 200으로 설정, 업데이트된 멤버 정보를 클라이언트에게 반환
            return ResponseEntity.status(HttpStatus.OK).body(updatedMember);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null) {
            // 로그인 성공
            session.setAttribute("userId", loginResult.getUserId());
            session.setAttribute("loginEmail", loginResult.getEmail());
            session.setAttribute("loginNickname", loginResult.getNickname());
            session.setAttribute("loginPassword", loginResult.getPassword());
            session.setAttribute("userAge", loginResult.getAge());
            session.setAttribute("userGender", loginResult.getGender());
            session.setAttribute("userInterests", loginResult.getMood());
            session.setAttribute("userAgreements", loginResult.getAgreements());
            session.setAttribute("loginMethod", loginResult.getLoginMethod());
            System.out.println(session.getAttribute("loginNickname") + "님 로그인 성공");
            System.out.println("loginResult = " + loginResult);
            System.out.println("Session ID: " + session.getId()); // session ID 출력
            return ResponseEntity.ok(loginResult); // 로그인 성공시 memberDTO 객체 반환
        } else {
            // login 실패
            return ResponseEntity.badRequest().body("로그인 실패"); // 응답 상태 코드 400
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 현재 세션을 무효화
        System.out.println("로그아웃 완료");
        return ResponseEntity.ok("로그아웃 성공");
    }
}
