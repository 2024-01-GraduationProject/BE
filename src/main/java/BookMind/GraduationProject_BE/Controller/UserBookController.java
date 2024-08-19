package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.Entity.UserBook;
import BookMind.GraduationProject_BE.Service.UserBookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookshelf")
@RequiredArgsConstructor
public class UserBookController {

    private static final Logger logger = LoggerFactory.getLogger(UserBookController.class);

    private final UserBookService userBookService;

    @PostMapping("/addBook")
    public ResponseEntity<UserBook> addBookToShelf(@RequestParam("userId") Long userId, @RequestParam("bookId") Long bookId) {
        logger.info("책을 독서 중 목록에 추가: userId: {}, bookId: {}", userId, bookId);
        UserBook userBook = userBookService.addBookToShelf(userId, bookId);

        return ResponseEntity.ok(userBook);
    }

    @GetMapping("/reading")
    public ResponseEntity<List<UserBook>> getReadingBooks(@RequestParam("userId") Long userId) {
        logger.info("독서 중 목록 조회: userId: {}", userId);

        List<UserBook> readingBooks = userBookService.getReadingBooks(userId);

        return ResponseEntity.ok(readingBooks);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<UserBook>> getCompletedBooks(@RequestParam("userId") Long userId) {
        logger.info("독서 완료 목록 조회: userId: {}", userId);

        List<UserBook> completedBooks = userBookService.getCompletedBooks(userId);

        return ResponseEntity.ok(completedBooks);
    }

    @PutMapping("/completeBook")
    public ResponseEntity<Void> markAsCompleted(@RequestParam("userId") Long userId, @RequestParam("bookId") Long bookId) {
        logger.info("책을 독서 완료로 표시: userId: {}, bookId: {}", userId, bookId);

        userBookService.markAsCompleted(userId, bookId);

        return ResponseEntity.noContent().build();
    }
}
