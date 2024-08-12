package BookMind.GraduationProject_BE.Service;


import BookMind.GraduationProject_BE.DTO.BookmarkDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Entity.Bookmark;
import BookMind.GraduationProject_BE.Entity.UserBook;
import BookMind.GraduationProject_BE.Repository.BookRepository;
import BookMind.GraduationProject_BE.Repository.BookmarkRepository;
import BookMind.GraduationProject_BE.Repository.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;

    // 즐겨찾기 추가
    public BookmarkDTO addBookmark(Long userbookId) {
        UserBook userBook = userBookRepository.findById(userbookId)
                .orElseThrow(() -> new NoSuchElementException("UserBook not found"));

        Bookmark bookmark = new Bookmark();
        bookmark.setUserbookId(userbookId);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return convertToDTO(savedBookmark);
    }

    // 즐겨찾기 목록 조회
    public List<BookmarkDTO> getUserBookmarks(Long userId) {
        return bookmarkRepository.findAllByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BookmarkDTO convertToDTO(Bookmark bookmark) {
        UserBook userBook = userBookRepository.findById(bookmark.getUserbookId())
                .orElseThrow(() -> new NoSuchElementException("UserBook not found"));

        Book book = bookRepository.findById(userBook.getBookId())
                .orElseThrow(() -> new NoSuchElementException("Book not found"));

        BookmarkDTO dto = new BookmarkDTO();
        dto.setBookmarkId(bookmark.getBookmarkId());
        dto.setUserbookId(userBook.getUserbookId());
        dto.setBookId(userBook.getBookId());
        dto.setStatus(userBook.getStatus().name());
        dto.setFavorite(userBook.getFavorite());
        dto.setLastReadPage(userBook.getLastReadPage());
        dto.setStartDate(userBook.getStartDate() != null ? userBook.getStartDate().toString() : null);
        dto.setEndDate(userBook.getEndDate() != null ? userBook.getEndDate().toString() : null);
        dto.setRating(userBook.getRating());

        // Book book = bookRepository.findById(userBook.getBookId()).orElseThrow(() -> new NoSuchElementException("Book not found"));
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCategory(book.getCategory());

        return dto;
    }
}
