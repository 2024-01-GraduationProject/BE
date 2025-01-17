package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.DTO.UserBookDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Entity.UserBook;
import BookMind.GraduationProject_BE.Entity.UserBookIndices;
import BookMind.GraduationProject_BE.Repository.BookRepository;
import BookMind.GraduationProject_BE.Repository.UserBookIndicesRepository;
import BookMind.GraduationProject_BE.Repository.UserBookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBookService {

    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;
    private final UserBookIndicesRepository userBookIndicesRepository;
    private final GPTService gptService;

    private static final Logger logger = LoggerFactory.getLogger(UserBookService.class);

    // 즐겨찾기 추가
    public UserBookDTO addBookmark(Long userId, Long bookId) {
        logger.info("사용자 ID: {}, 책 ID: {}에 대해 즐겨찾기 추가.", userId, bookId);

        // userbookId 생성
        String userbookId = userId + "-" + bookId;

        // UserBook 엔터티 확인 또는 생성
        UserBook userBook = userBookRepository.findByUserIdAndBookId(userId, bookId)
                .orElseGet(() -> {
                    UserBook newUserBook = new UserBook();
                    newUserBook.setUserbookId(userbookId);
                    newUserBook.setUserId(userId);
                    newUserBook.setBookId(bookId);
                    newUserBook.setFavorite(true); // 즐겨찾기만 추가
                    newUserBook.setStatus(null); // 상태를 설정하지 않음
                    return userBookRepository.save(newUserBook);
                });

        // 이미 존재하는 UserBook이라면, favorite만 업데이트
        if (!userBook.getFavorite()) {
            userBook.setFavorite(true);
            userBookRepository.save(userBook);
        }

        logger.info("UserBook 엔터티 확인. UserBook ID: {}", userBook.getUserbookId());

        return convertToDTO(userBook);
    }

    // 즐겨찾기 목록 조회
    public List<UserBookDTO> getUserBookmarks(Long userId) {
        logger.info("사용자 ID: {}에 대한 즐겨찾기 목록을 조회.", userId);

        // 사용자 ID로 favorite이 true인 항목들만 조회
        List<UserBookDTO> bookmarks = userBookRepository.findAllByUserIdAndFavoriteTrue(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        logger.info("총 {}개의 즐겨찾기 항목을 찾음.", bookmarks.size());

        return bookmarks.isEmpty() ? List.of() : bookmarks;
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

        logger.info("UserBook ID: {}에 대한 즐겨찾기 삭제 성공.", userbookId);
    }


    // 책을 독서 중 상태로 추가
    public UserBook addBookToShelf(Long userId, Long bookId, Date startDate) {
        logger.info("사용자 ID: {}, 책 ID: {}을 독서 중으로 추가. 시작 날짜: {}", userId, bookId, startDate);

        String userbookId = userId + "-" + bookId;

        UserBook userBook = userBookRepository.findById(userbookId)
                .orElseGet(() -> {
                    UserBook newUserBook = new UserBook();
                    newUserBook.setUserbookId(userbookId);
                    newUserBook.setUserId(userId);
                    newUserBook.setBookId(bookId);
                    newUserBook.setStatus(UserBook.Status.READING); // 독서 중 상태 설정
                    // 기본값 설정
                    newUserBook.setLastReadPage(0.0f); // 초기 진도율 설정
                    newUserBook.setStartDate(startDate);
                    newUserBook.setEndDate(null);
                    newUserBook.setRating(null);
                    return userBookRepository.save(newUserBook);
                });

        // 이미 존재하는 UserBook이 있을 때 상태를 READING으로 업데이트
        if (userBook.getStatus() == null || !userBook.getStatus().equals(UserBook.Status.READING)) {
            userBook.setStatus(UserBook.Status.READING);
            userBookRepository.save(userBook);
        }

        logger.info("UserBook 생성 또는 조회 성공. UserBook ID: {}", userBook.getUserbookId());
        return userBook;
    }

    // 진도율 업데이트 및 인덱스 저장 메서드 (독서 중 진행 상황 업데이트 시 호출)
    @Transactional
    public void updateReadingProgress(Long userId, Long bookId, float lastReadPage, List<Float> indices) {
        logger.info("사용자 ID: {}, 책 ID: {}의 읽기 진행 상태 업데이트", userId, bookId);
        String userbookId = userId + "-" + bookId;
        UserBook userBook = userBookRepository.findById(userbookId)
                .orElseThrow(() -> new NoSuchElementException("UserBook not found"));

        // 진도율 업데이트
        userBook.setLastReadPage(lastReadPage);

        // 인덱스 리스트 저장 메서드 호출
        saveUserBookIndices(userBook, indices);

        // 독서 중 진행 상황 저장
        userBookRepository.save(userBook);
    }

    public List<UserBook> getReadingBooks(Long userId) {
        logger.info("사용자 ID: {}의 독서 중 목록을 조회", userId);
        return userBookRepository.findAllByUserIdAndStatus(userId, UserBook.Status.READING);
    }

    public List<UserBook> getCompletedBooks(Long userId) {
        logger.info("사용자 ID: {}의 독서 완료 목록을 조회", userId);
        return userBookRepository.findAllByUserIdAndStatus(userId, UserBook.Status.COMPLETED);
    }

    // 저장된 인덱스 및 마지막 읽은 페이지 조회
    public UserBookDTO getReadingProgress(Long userId, Long bookId) {
        logger.info("사용자 ID: {}, 책 ID: {}의 읽기 진행 상태 조회", userId, bookId);
        String userbookId = userId + "-" + bookId;
        UserBook userBook = userBookRepository.findById(userbookId)
                .orElseThrow(() -> new NoSuchElementException("UserBook not found"));

        // 수정된 레포지토리 메서드 사용
        List<Float> indexPages = userBookIndicesRepository.findAllByUserBook(userBook).stream()
                .map(UserBookIndices::getIndexPage)
                .collect(Collectors.toList());

        UserBookDTO dto = convertToDTO(userBook);
        dto.setIndexPages(indexPages);

        return dto;
    }

    // 진도율 업데이트 및 인덱스 추가 기능
    @Transactional
    public void markAsCompleted(Long userId, Long bookId, float lastReadPage, List<Float> indices) {
        logger.info("사용자 ID: {}, 책 ID: {}을 독서 완료로 변경", userId, bookId);
        String userbookId = userId + "-" + bookId;
        UserBook userBook = userBookRepository.findById(userbookId)
                .orElseThrow(() -> new NoSuchElementException("UserBook not found"));

        // 진도율 업데이트
        userBook.setLastReadPage(lastReadPage);

        // 인덱스 리스트 저장 메서드 호출
        saveUserBookIndices(userBook, indices);

        // 진도율이 100일 경우 상태를 COMPLETED로 변경
        if (lastReadPage >= 100.0f) {
            userBook.setStatus(UserBook.Status.COMPLETED);
            userBook.setEndDate(new java.sql.Date(System.currentTimeMillis()));
            logger.info("독서 완료로 변경 성공: {}", userbookId);

            // 독서 완료 후 GPT 질문 생성
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new NoSuchElementException("책을 찾을 수 없습니다."));

            String question = gptService.generateQuestion(book.getTitle());

            // "부기"가 사용자에게 질문하는 로직 추가
            System.out.println("부기가 사용자에게 질문합니다: " + question);
        }

        userBookRepository.save(userBook);

    }

    // 인덱스 페이지 저장 메서드
    public void saveUserBookIndices(UserBook userBook, List<Float> indices) {
        for (Float index : indices) {
            logger.info("저장할 인덱스: {}", index); // 인덱스 로그 추가
            if (!userBookIndicesRepository.existsByUserBookAndIndexPage(userBook, index)) {
                UserBookIndices newIndex = new UserBookIndices(userBook, index);
                newIndex.setUserBook(userBook);
                userBookIndicesRepository.save(newIndex);  // 인덱스 저장
                logger.info("인덱스 저장: " + index); // 저장 로그
            } else {
                logger.info("인덱스가 이미 존재합니다: {}", index);
            }
        }
    }

    // 이 달의 독서량
    public int monthlyRate(Long userId) {
        // 사용자의 도서 기록
        List<UserBook> userBooks = userBookRepository.findByUserId(userId);

        // 현재 연도와 월
        YearMonth currentYearMonth = YearMonth.now();

        // 현재 달에 해당하는 도서 기록 중 완독한 도서 기록 (end_date 기준)
        List<UserBook> booksReadThisMonth = userBooks.stream()
                .filter(book -> book.getEndDate() != null) // end_date가 null이 아닌 (완독된) 도서만 필터링
                .filter(book -> {
                    // java.sql.Date를 java.time.LocalDate로 변환
                    LocalDate endDate = book.getEndDate().toLocalDate();
                    return YearMonth.from(endDate).equals(currentYearMonth); // end_date가 이번 달에 해당하는지 체크
                })
                .collect(Collectors.toList());

        // 이번 달에 완독한 도서 권수
        return booksReadThisMonth.size();
    }

    private UserBookDTO convertToDTO(UserBook userBook) {
        Book book = bookRepository.findById(userBook.getBookId())
                .orElseThrow(() -> new NoSuchElementException("책을 찾을 수 없습니다."));

        UserBookDTO dto = new UserBookDTO();
        dto.setUserbookId(userBook.getUserbookId());
        dto.setBookId(userBook.getBookId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCategory(book.getCategory());
        dto.setStatus(userBook.getStatus() != null ? userBook.getStatus().name() : null);
        dto.setFavorite(userBook.getFavorite());
        dto.setLastReadPage(userBook.getLastReadPage());
        dto.setStartDate(userBook.getStartDate() != null ? userBook.getStartDate().toString() : null);
        dto.setEndDate(userBook.getEndDate() != null ? userBook.getEndDate().toString() : null);
        dto.setRating(userBook.getRating());
        // 인덱스 리스트 설정
        List<Float> indexPages = userBookIndicesRepository.findAllByUserBook(userBook)
                .stream()
                .map(UserBookIndices::getIndexPage)
                .collect(Collectors.toList());
        dto.setIndexPages(indexPages);

        return dto;
    }

}
