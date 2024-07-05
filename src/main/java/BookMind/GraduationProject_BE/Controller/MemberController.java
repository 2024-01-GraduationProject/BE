package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000") // CORS 설정
@Controller
@RequiredArgsConstructor
public class MemberController {

    // 로그를 기록하기 위한 로그 객체 생성
    //private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private final MemberService memberService;

    // 회원가입 폼(Get 요청)
    @GetMapping("/register")
    public String saveForm() {
        System.out.println("프론트에서 회원가입 폼 받아오기 성공");
        return "register";} // register 이름을 가진 뷰가 렌더링 됨

    // 회원가입 폼 제출(Post 요청) (회원 데이터를 등록)
    @PostMapping("/register")
    public ResponseEntity<?> save(@RequestBody MemberDTO memberDTO) {
        //logger.info("MemberController.save - memberDTO = {}", memberDTO);

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
}
