package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.BookmarkDTO;
import BookMind.GraduationProject_BE.Entity.Bookmark;
import BookMind.GraduationProject_BE.Entity.UserBook;
import BookMind.GraduationProject_BE.Repository.UserBookRepository;
import BookMind.GraduationProject_BE.Service.BookmarkService;
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

    private final BookmarkService bookmarkService;
    private final UserBookRepository userBookRepository;


    // 책을 즐겨찾기에 추가
    @PostMapping("/addBook")
    public ResponseEntity<BookmarkDTO> addBookmark(@RequestParam("userbookId") String userbookId) {
        logger.info("사용자의 책을 즐겨찾기 추가. userbookId: {}", userbookId);
        try {
            // userbookId를 userId와 bookId로 분리
            String[] ids = userbookId.split("-");
            if (ids.length != 2) {
                throw new IllegalArgumentException("userbookId 형식이 잘못되었습니다. 올바른 형식: userId-bookId.");
            }

            Long userId = Long.parseLong(ids[0]);
            Long bookId = Long.parseLong(ids[1]);

            BookmarkDTO bookmarkDTO = bookmarkService.addBookmark(userId, bookId);
            logger.info("즐겨찾기 추가 성공: userbookId: {}", bookmarkDTO.getUserbookId());
            return ResponseEntity.ok(bookmarkDTO);
        } catch (NumberFormatException e) {
            logger.error("userId 또는 bookId를 Long 타입으로 변환하는 데 실패했습니다: {}", e.getMessage());
            return ResponseEntity.status(400).body(null); // 잘못된 요청 반환
        } catch (Exception e) {
            logger.error("즐겨찾기 추가 실패: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    // 마이페이지에서 즐겨찾기한 책 목록 조회
    @GetMapping("/user/{userbookId}")
    public ResponseEntity<List<BookmarkDTO>> getUserBookmarks(@PathVariable("userbookId") String userbookId) {
        logger.info("사용자의 즐겨찾기 목록을 조회합니다. userbookId: {}", userbookId);
        try {
            List<BookmarkDTO> bookmarks = bookmarkService.getUserBookmarks(userbookId);
            logger.info("즐겨찾기 목록 조회 성공. 항목 수: {}", bookmarks.size());
            return ResponseEntity.ok(bookmarks);
        } catch (Exception e) {
            logger.error("즐겨찾기 목록 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    // 책을 즐겨찾기에서 제거
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeBookmark(@RequestParam("userbookId") String userbookId) {
        logger.info("사용자의 책을 즐겨찾기에서 삭제. userbookId: {}", userbookId);
        try {
            bookmarkService.removeBookmark(userbookId);
            logger.info("즐겨찾기 제거 성공: userbookId: {}", userbookId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            logger.error("즐겨찾기 제거 실패: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            logger.error("즐겨찾기 제거 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }


}
