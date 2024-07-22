package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.InformationAndTasteDTO;
import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.Entity.Age;
import BookMind.GraduationProject_BE.Entity.BookCategory;
import BookMind.GraduationProject_BE.Entity.Gender;
import BookMind.GraduationProject_BE.Entity.Member;
import BookMind.GraduationProject_BE.Repository.AgeRepository;
import BookMind.GraduationProject_BE.Repository.BookCategoryRepository;
import BookMind.GraduationProject_BE.Repository.GenderRepository;
import BookMind.GraduationProject_BE.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class MemberController {

    // 로그를 기록하기 위한 로그 객체 생성
    //private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private final MemberService memberService;
    @Autowired
    private final AgeRepository ageRepository;
    @Autowired
    private final GenderRepository genderRepository;
    @Autowired
    private final BookCategoryRepository bookCategoryRepository;

    // 프론트엔드에서 직접 회원가입 폼을 제공함. GetMapping 필요 없음.
//    @GetMapping("/register")
//    public String saveForm() {return "register";}

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

    // 연령 데이터를 조회
    @GetMapping("/ages")
    public ResponseEntity<List<Age>> getAllAges() {
        List<Age> ages = ageRepository.findAll();
        System.out.println("연령 데이터 불러오기 성공");
        return new ResponseEntity<>(ages, HttpStatus.OK);
    }
    // 성별 데이터를 조회
    @GetMapping("/genders")
    public ResponseEntity<List<Gender>> getAllGenders() {
        List<Gender> genders = genderRepository.findAll();
        System.out.println("성별 데이터 불러오기 성공");
        return new ResponseEntity<>(genders, HttpStatus.OK);
    }
    // 책 카테고리 데이터를 조회
    @GetMapping("/book-categories")
    public ResponseEntity<List<BookCategory>> getAllBookCategories() {
        List<BookCategory> bookCategories = bookCategoryRepository.findAll();
        System.out.println("책 카테고리 데이터 불러오기 성공");
        return new ResponseEntity<>(bookCategories, HttpStatus.OK);
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

    // 프론트엔드에서 직접 로그인 폼 제공함.
//    @GetMapping("/login")
//    public String loginForm() { return "login"; }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null) {
            // 로그인 성공
            session.setAttribute("loginEmail", loginResult.getEmail());
            session.setAttribute("loginNickname", loginResult.getNickname());
            System.out.println(loginResult.getNickname() + "님 로그인 성공");
            return ResponseEntity.ok(loginResult); // 로그인 성공시 memberDTO 객체 반환
        } else {
            // login 실패
            return ResponseEntity.badRequest().body("로그인 실패");
        }
    }

}
