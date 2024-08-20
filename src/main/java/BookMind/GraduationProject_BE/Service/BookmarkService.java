package BookMind.GraduationProject_BE.Service;


import BookMind.GraduationProject_BE.DTO.BookmarkDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Entity.Bookmark;
import BookMind.GraduationProject_BE.Entity.UserBook;
import BookMind.GraduationProject_BE.Repository.BookRepository;
import BookMind.GraduationProject_BE.Repository.BookmarkRepository;
import BookMind.GraduationProject_BE.Repository.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    // 로그 설정
    private static final Logger logger = LoggerFactory.getLogger(BookmarkService.class);


    // 즐겨찾기 추가
    public BookmarkDTO addBookmark(Long userId, Long bookId) {
        logger.info("사용자 ID: {}, 책 ID: {}에 대해 즐겨찾기 추가.", userId, bookId);

        // userbookId 생성
        String userbookId = userId + "-" + bookId;

        // UserBook 엔터티 확인 또는 생성
        UserBook userBook = userBookRepository.findById(userbookId)
                .orElseGet(() -> {
                    UserBook newUserBook = new UserBook();
                    newUserBook.setUserbookId(userbookId);
                    newUserBook.setUserId(userId);
                    newUserBook.setBookId(bookId);
                    newUserBook.setFavorite(true); // 즐겨찾기만 추가
                    return userBookRepository.save(newUserBook);
                });

        // 이미 존재하는 UserBook이라면, favorite만 업데이트
        if (!userBook.getFavorite()) {
            userBook.setFavorite(true);
            userBookRepository.save(userBook);
        }

        logger.info("UserBook 엔터티 확인. UserBook ID: {}", userBook.getUserbookId());

        // Bookmark 추가
        Bookmark bookmark = new Bookmark();
        bookmark.setUserbookId(userbookId);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        logger.info("즐겨찾기 추가 성공. Bookmark ID: {}", savedBookmark.getBookmarkId());

        return convertToDTO(savedBookmark);
    }

    // 즐겨찾기 목록 조회
    public List<BookmarkDTO> getUserBookmarks(String userbookId) {
        logger.info("UserBook ID: {}에 대한 즐겨찾기 목록을 조회.", userbookId);

        // UserBook 엔터티 확인
        UserBook userBook = userBookRepository.findById(userbookId)
                .orElseThrow(() -> {
                    logger.error("UserBook ID: {}에 대한 정보를 찾을 수 없습니다.", userbookId);
                    return new NoSuchElementException("UserBook not found");
                });

        if (!userBook.getFavorite()) {
            logger.info("UserBook ID: {}는 즐겨찾기 목록에 포함되지 않음.", userbookId);
            return List.of();  // 빈 목록 반환
        }

        List<BookmarkDTO> bookmarks = bookmarkRepository.findAllByUserbookId(userbookId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("총 {}개의 즐겨찾기.", bookmarks.size());
        return bookmarks;
    }

    // 즐겨찾기 해제
    public void removeBookmark(String userbookId) {
        logger.info("UserBook ID: {}에 대한 즐겨찾기 해제.", userbookId);

        UserBook userBook = userBookRepository.findById(userbookId)
                .orElseThrow(() -> {
                    logger.error("UserBook ID: {}에 대한 정보를 찾을 수 없습니다.", userbookId);
                    return new NoSuchElementException("UserBook not found");
                });

        // favorite 값을 false로 설정
        userBook.setFavorite(false);
        userBookRepository.save(userBook);

        // Bookmark 엔터티 삭제
        bookmarkRepository.findByUserbookId(userbookId)
                .ifPresent(bookmarkRepository::delete);

        logger.info("UserBook ID: {}에 대한 즐겨찾기 삭제 성공.", userbookId);
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

        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCategory(book.getCategory());

        return dto;
    }
}
