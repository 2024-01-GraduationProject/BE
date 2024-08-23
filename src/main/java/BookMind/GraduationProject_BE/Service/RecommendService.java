package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.DTO.BookDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Entity.Member;
import BookMind.GraduationProject_BE.Entity.UserBook;
import BookMind.GraduationProject_BE.Repository.BookRepository;
import BookMind.GraduationProject_BE.Repository.MemberRepository;
import BookMind.GraduationProject_BE.Repository.UserBookRepository;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // 메서드들이 읽기 전용 트랜잭션으로 실행됨
@RequiredArgsConstructor
public class RecommendService {

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final UserBookRepository userBookRepository;
    @Autowired
    private final BookRepository bookRepository;

    // 사용자와 같은 연령 및 성별을 가진 회원들의 userBook 내의 도서 데이터 조회
    public List<BookDTO> getBooks(String userAge, String userGender) {
        // 1. 사용자와 같은 연령 및 성별을 가진 회원들 조회
        List<Member> members = memberRepository.findByAgeAndGender(userAge, userGender);

        // 2. 회원들의 userId 추출
        List<Long> memberIds = members.stream()
                                        .map(Member::getUserId) // Member 엔티티에서 Id 추출
                                        .collect(Collectors.toList());

        // 3. 해당 회원들(memberIds)의 UserBook 조회
        List<UserBook> userBooks = userBookRepository.findByUserIdIn(memberIds);

        // 4. UserBook에서 book_id 추출
        List<Long> bookIds = userBooks.stream()
                                        .map(UserBook::getBookId) // UserBook에서 book_id를 추출
                                        .collect(Collectors.toList());

        // 5. bookIds로 Books 테이블에서 도서를 조회
        List<Book> books = bookRepository.findByBookIdIn(bookIds);

        // 6. books를 BookDTO로 변환
        List<BookDTO> bookDTOs = books.stream()
                                        .map(book -> {
                                            BookDTO bookDTO = new BookDTO();
                                            bookDTO.setBookId(book.getBookId());
                                            bookDTO.setTitle(book.getTitle());
                                            bookDTO.setAuthor(book.getAuthor());
                                            bookDTO.setPublisher(book.getPublisher());
                                            bookDTO.setCategory(book.getCategory());
                                            bookDTO.setCoverImageUrl(book.getCoverImageUrl());
                                            return bookDTO;
                                        })
                                        .collect(Collectors.toList());

        return bookDTOs; // 최종적으로 도서 목록 반환
    }

}
