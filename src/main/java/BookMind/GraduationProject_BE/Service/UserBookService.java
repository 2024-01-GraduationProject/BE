package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.Entity.UserBook;
import BookMind.GraduationProject_BE.Repository.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserBookService {

    private final UserBookRepository userBookRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserBookService.class);

    // 책을 독서 중 상태로 추가
    public UserBook addBookToShelf(Long userId, Long bookId) {
        logger.info("사용자 ID: {}, 책 ID: {}을 독서 중으로 추가", userId, bookId);

        String userbookId = userId + "-" + bookId;

        UserBook userBook = userBookRepository.findById(userbookId)
                .orElseGet(() -> {
                    UserBook newUserBook = new UserBook();
                    newUserBook.setUserbookId(userbookId);
                    newUserBook.setUserId(userId);
                    newUserBook.setBookId(bookId);
                    newUserBook.setStatus(UserBook.Status.READING);
                    // 기본값 설정
                    newUserBook.setFavorite(false);
                    newUserBook.setLastReadPage(0);
                    newUserBook.setStartDate(null);
                    newUserBook.setEndDate(null);
                    newUserBook.setRating(null);
                    return userBookRepository.save(newUserBook);
                });

        logger.info("UserBook 생성 또는 조회 성공. UserBook ID: {}", userBook.getUserbookId());
        return userBook;
    }

    public List<UserBook> getReadingBooks(Long userId) {
        logger.info("사용자 ID: {}의 독서 중 목록을 조회", userId);
        return userBookRepository.findAllByUserIdAndStatus(userId, UserBook.Status.READING);
    }

    public List<UserBook> getCompletedBooks(Long userId) {
        logger.info("사용자 ID: {}의 독서 완료 목록을 조회", userId);
        return userBookRepository.findAllByUserIdAndStatus(userId, UserBook.Status.COMPLETED);
    }

    public void markAsCompleted(Long userId, Long bookId) {
        logger.info("사용자 ID: {}, 책 ID: {}을 독서 완료로 변경", userId, bookId);
        String userbookId = userId + "-" + bookId;
        UserBook userBook = userBookRepository.findById(userbookId)
                .orElseThrow(() -> new NoSuchElementException("UserBook not found"));

        userBook.setStatus(UserBook.Status.COMPLETED);
        userBook.setEndDate(new java.sql.Date(System.currentTimeMillis()));
        userBookRepository.save(userBook);
        logger.info("독서 완료로 변경 성공: {}", userbookId);
    }

}
