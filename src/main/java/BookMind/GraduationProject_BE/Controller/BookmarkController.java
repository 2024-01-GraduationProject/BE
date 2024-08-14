package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.BookmarkDTO;
import BookMind.GraduationProject_BE.Entity.Bookmark;
import BookMind.GraduationProject_BE.Service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 책을 즐겨찾기에 추가
    @PostMapping("/addBook")
    public ResponseEntity<BookmarkDTO> addBookmark(@RequestParam Long userbookId) {
        BookmarkDTO bookmarkDTO = bookmarkService.addBookmark(userbookId);
        return ResponseEntity.ok(bookmarkDTO);

    }

    // 마이페이지에서 즐겨찾기한 책 목록 조회
    @GetMapping("/user/{userbookId}")
    public ResponseEntity<List<BookmarkDTO>> getUserBookmarks(@PathVariable Long userbookId) {
        List<BookmarkDTO> bookmarks = bookmarkService.getUserBookmarks(userbookId);
        return ResponseEntity.ok(bookmarks);
    }

    // 책을 즐겨찾기에서 제거
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeBookmark(@RequestParam Long userbookId) {
        bookmarkService.removeBookmark(userbookId);
        return ResponseEntity.noContent().build();
    }


}
