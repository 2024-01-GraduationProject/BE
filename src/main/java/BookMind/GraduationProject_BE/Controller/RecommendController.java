package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.BookDTO;
import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Service.RecommendService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private final RecommendService recommendService;

    // 1. 개인화 추천 시스템 (TFRS 모델 사용)
    // 사용자의 취향 데이터와 독서 데이터를 바탕으로 한 추천
//    @GetMapping("/userTaste")
//    public ResponseEntity<List<BookDTO>> personalRecommend() {
//        // TFRS 모델의 추천 결과를 받아와서 프론트로 해당 추천 결과를 넘겨주는 로직
          // 추후 작성 예정 ---
//    }

    // 2. 연령대, 성별에 맞춘 추천 시스템 (모델없이 로직으로 구현)
    // 사용자와 같은 연령대와 성별인 사용자들의 독서 데이터를 바탕으로 추천
    @GetMapping("/ageAndGender")
    public ResponseEntity<List<BookDTO>> groupRecommend(HttpSession session) {
        // 0.세션 정보 확인
        System.out.println("Session ID: " + session.getId());

        // 1. 세션에 저장된 회원의 연령과 성별 정보를 불러옴
        MemberDTO userData = (MemberDTO) session.getAttribute("loginUser");
        if (userData == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인하지 않은 경우 401 응답
        }
        String userAge = userData.getAge();
        String userGender = userData.getGender();

        // 2. 회원과 같은 연령 및 성별 정보를 가진 회원들의 userBook의 책 정보를 불러옴
        List<BookDTO> recommendedBooks = recommendService.getBooks(userAge, userGender);

        // 3. 추천 도서 목록을 JSON 형태로 반환
        return ResponseEntity.ok(recommendedBooks);
    }
}
