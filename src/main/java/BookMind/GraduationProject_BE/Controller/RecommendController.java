package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.*;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Service.BookService;
import BookMind.GraduationProject_BE.Service.DataPreprocessingService;
import BookMind.GraduationProject_BE.Service.RecommendService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private final RecommendService recommendService;
    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final DataPreprocessingService dataPreprocessingService;
    @Autowired
    private final BookService bookService;

    // 1. 개인화 추천 시스템 (TFRS 모델 사용)
    // 사용자의 취향 데이터와 독서 데이터를 바탕으로 한 추천
    // 추천시스템 전처리 데이터 및 사용자 id >> Flask 서버로 반환
    // Flask 서버에서 모델과 추천 함수를 통해 추천된 도서를 백 서버로 반환
    // 백 서버에서는 해당 도서들의 id를 바탕으로 books 테이블에서 도서들을 찾아서 프론트로 반환
    @GetMapping("/userTaste")
    public ResponseEntity<?> getRecommendations(HttpSession session) {
        // 세션 정보 확인
        System.out.println("Session ID: " + session.getId());

        //세션에 저장된 userId를 불러옴
        MemberDTO userData = (MemberDTO) session.getAttribute("loginUser");
        if (userData == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("사용자 정보가 없습니다.");
        }

        Long userId = userData.getUserId();
        System.out.println("userId = " + userId);

        // 데이터 전처리
        List<Map<String, Object>> data = dataPreprocessingService.preprocessData();
        List<SimpleUserBookDTO> userBook  = dataPreprocessingService.getUserBook();
        List<SimpleBookDTO> book = dataPreprocessingService.getBook();

        // Flask API에 POST 요청 보내기
        String flaskUrl = "http://localhost:5000/recommend";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", userId); // 로그인된 사용자 id
        requestBody.put("data", data); // 전처리된 데이터
        requestBody.put("userBook", userBook); // userBook 데이터
        requestBody.put("book", book); // book 데이터

        HttpHeaders headers = new HttpHeaders(); // 요청에 대한 헤더 정보를 설정
        headers.setContentType(MediaType.APPLICATION_JSON); // 요청 본문이 JSON 형식임을 나타냄
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers); // HTTP 요청을 구성하는 객체

        // Flask API 호출
        ResponseEntity<Map> responseEntity = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, Map.class);
        Map<String, Object> recommendBooks = responseEntity.getBody();

        // 추천된 bookId를 받아서 books 테이블에서 찾기
        List<Long> bookIds = (List<Long>) recommendBooks.get("book_ids"); // book_ids는 Flask에서 반환되는 키
        List<BookDTO> recommendations = bookService.findBooksByIds(bookIds);

        // Flask로부터 받은 추천 결과를 프론트엔드에 반환
        return ResponseEntity.ok(recommendations);
    }

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
