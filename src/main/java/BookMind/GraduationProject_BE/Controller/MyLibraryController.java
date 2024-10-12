package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.DTO.MonthlyReadingDTO;
import BookMind.GraduationProject_BE.Service.MemberService;
import BookMind.GraduationProject_BE.Service.UserBookService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyLibraryController {

    @Autowired
    private final UserBookService userBookService;

    // 내 서재 _ 사용자 닉네임 불러오기
    @GetMapping("/user-nickname")
    public ResponseEntity<?> getNickname(HttpSession session) {
        System.out.println("Session ID: " + session.getId());

        // 세션에 저장된 사용자의 정보를 불러옴
        MemberDTO userData = (MemberDTO) session.getAttribute("loginUser");
        String loginNickname = userData.getNickname();

        // 로그인 상태 확인 _ 세션에 등록된 사용자가 아닌 경우
        if (loginNickname == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 된 사용자가 아닙니다.");
        }

        // 닉네임을 정상적으로 가져온 경우
        return ResponseEntity.ok(loginNickname);
    }

    // 이번 달에 완독한 도서 권수
    @GetMapping("/monthlyReading")
    public ResponseEntity<?> getMonthlyReadingCount(HttpSession session) {

        //세션에 저장된 userId를 불러옴
        MemberDTO userData = (MemberDTO) session.getAttribute("loginUser");
        if (userData == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("사용자 정보가 없습니다.");
        }

        Long userId = userData.getUserId();

        // 현재 달 가져오기
        int currentMonth = LocalDate.now().getMonthValue();

        // 이번 달의 독서 권수
        int currentMonthReading = userBookService.monthlyRate(userId);

        MonthlyReadingDTO response = new MonthlyReadingDTO(currentMonth, currentMonthReading);

        // 현재 달과 함께 이번 달의 독서량 넘김
        return ResponseEntity.ok(response);
    }
}
