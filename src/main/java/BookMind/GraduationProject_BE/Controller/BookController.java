package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.BookDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Service.BookService;
import lombok.RequiredArgsConstructor; // Lombok을 사용한 생성자 자동 주입
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // 모든 책 목록 조회
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        List<BookDTO> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    // 책 상세 정보 조회
    @GetMapping("/{book_id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("book_id") Long bookId){
        try {
            BookDTO book = bookService.getBookById(bookId);
            if (book != null) {
                return ResponseEntity.ok(book);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
