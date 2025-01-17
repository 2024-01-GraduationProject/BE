package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.BookDTO;
import BookMind.GraduationProject_BE.DTO.BookWithCategoriesDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Service.BookCategoryService;
import BookMind.GraduationProject_BE.Service.BookService;
import BookMind.GraduationProject_BE.Service.SearchService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor; // Lombok을 사용한 생성자 자동 주입
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private SearchService searchService;
    @Autowired
    private BookCategoryService bookCategoryService;

    // 모든 책 목록 조회
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        List<BookDTO> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    // 책 상세 정보 조회
    @GetMapping("/{book_id}")
    public ResponseEntity<BookWithCategoriesDTO> getBookById(@PathVariable("book_id") Long bookId) {
        try {
            // 책 정보 조회
            BookDTO book = bookService.getBookById(bookId);
            // 해당 책의 카테고리 정보 조회
            List<String> bookCategory = bookCategoryService.getCategoryName(bookId);

            if (book != null) {
                // BookDTO와 카테고리 리스트를 포함하는 BookWithCategoryDTO 생성
                BookWithCategoriesDTO response = new BookWithCategoriesDTO(book, bookCategory);
                System.out.println("bookCategories = " + bookCategory);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 책의 epub 파일 조회
    @GetMapping("/{book_id}/content")
    public ResponseEntity<InputStreamResource> getBookContent(@PathVariable("book_id") Long bookId) throws IOException {
        BookDTO book = bookService.getBookById(bookId);

        // 책 정보가 없으면 404 NOT FOUND 응답 반환
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        // 파일 경로 및 존재 확인
        String basePath = Paths.get("BE","src", "main", "resources", "static").toString();
        String relativeFilePath = book.getContent();
        File file = new File(basePath, relativeFilePath);

        //File file = new File(book.getContent());

        if (!file.exists()) {
            System.err.println("File not found: " + file.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        // 파일 읽기 권한 확인
        if (!file.canRead()) {
            System.err.println("Cannot read file: " + file.getAbsolutePath());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        // 파일을 읽어서 반환
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.parseMediaType("application/epub+zip"))
                .contentLength(file.length())
                .body(resource);
    }

    // 카테고리 별로 구분
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<BookDTO>> getBooksByCategory(@PathVariable String categoryName) {
        List<BookDTO> books = bookService.getBooksByCategoryName(categoryName);
        return ResponseEntity.ok(books);
    }

    // 검색어를 통한 도서 조회
    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam Object searchWord) {
        System.out.println("searchWord = " + searchWord);
        List<BookDTO> searchResults = searchService.searchBooks(searchWord);
        System.out.println("searchResults = " + searchResults);
        return ResponseEntity.ok(searchResults);
    }

    // 베스트 도서 조회
    @GetMapping("/best")
    public ResponseEntity<List<BookDTO>> bestBooks() {
        List<BookDTO> best4Books = bookService.findBestBooksByIds();
        System.out.println("best4Books = " + best4Books);
        return ResponseEntity.ok(best4Books);
    }
}
