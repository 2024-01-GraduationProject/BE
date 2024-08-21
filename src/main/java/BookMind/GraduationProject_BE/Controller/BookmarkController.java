package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.UserBookDTO;
import BookMind.GraduationProject_BE.Service.UserBookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkController.class);
    private final UserBookService userBookService;

    // 책을 즐겨찾기에 추가
    @PostMapping("/addBook")
    public ResponseEntity<UserBookDTO> addBookmark(@RequestParam("userId") Long userId, @RequestParam("bookId") Long bookId) {
        logger.info("사용자 ID: {}, 책 ID: {}에 대해 즐겨찾기 추가.", userId, bookId);
        try {
            UserBookDTO userBookDTO = userBookService.addBookmark(userId, bookId);
            return ResponseEntity.ok(userBookDTO);
        } catch (Exception e) {
            logger.error("즐겨찾기 추가 실패: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }


    // 마이페이지에서 즐겨찾기한 책 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<UserBookDTO>> getBookmarks(@RequestParam("userId") Long userId) {
        logger.info("사용자 ID: {}의 즐겨찾기 목록 조회.", userId);
        List<UserBookDTO> bookmarks = userBookService.getUserBookmarks(userId);
        return ResponseEntity.ok(bookmarks);
    }

    // 책을 즐겨찾기에서 제거
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeBookmark(@RequestParam("userId") Long userId, @RequestParam("bookId") Long bookId) {
        logger.info("사용자 ID: {}, 책 ID: {}에 대해 즐겨찾기 해제.", userId, bookId);
        try {
            String userbookId = userId + "-" + bookId; // userbookId 생성
            userBookService.removeBookmark(userbookId); // userbookId를 전달
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            logger.error("즐겨찾기 해제 실패 - 해당 항목을 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.status(404).build(); // 항목이 없을 경우 404 반환
        } catch (Exception e) {
            logger.error("즐겨찾기 해제 실패: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }


}
