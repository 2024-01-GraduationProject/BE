package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.DTO.BookDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Entity.BookCategoryConnection;
import BookMind.GraduationProject_BE.Repository.BookCategoryConnectionRepository;
import BookMind.GraduationProject_BE.Repository.BookRepository;
import BookMind.GraduationProject_BE.Repository.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final UserBookRepository userBookRepository;
    @Autowired
    private final BookCategoryConnectionRepository bookCategoryConnectionRepository;

    @Transactional(readOnly = true)
    public List<BookDTO> findAllBooks(){
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ID로 책 정보 조회
    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }


    // 카테고리 별 책 분류
    public List<BookDTO> getBooksByCategoryName(String categoryName) {
        List<Book> books = bookRepository.findBooksByCategoryName(categoryName);
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // bookId들로 책 리스트 조회
    public List<BookDTO> findBooksByIds(List<Long> bookIds) {
        List<Book> books = bookRepository.findByBookIdIn(bookIds);
        List<BookDTO> bookDTOs = books.stream()
                .map(this::convertToDTO).collect(Collectors.toList());

        return bookDTOs;
    }

    // (베스트 도서 조회) UserBook에서 추출한 bookId로 책 리스트 조회
    public List<BookDTO> findBestBooksByIds() {
        // 1. UserBook에서 도서 Id 받아오기
        List<Long> bookIds = userBookRepository.findMostReadBookIds();
        // 2. Book 테이블에서 도서 조회
        List<Book> bestBooks = bookRepository.findByBookIdIn(bookIds);
        // 3. BookDTO로 변환
        List<BookDTO> bestBookDTOs = bestBooks.stream()
                .map(this::convertToDTO).collect(Collectors.toList());

        return bestBookDTOs;
    }

    private BookDTO convertToDTO(Book book) {
        // DTO 변환 로직
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setPublisher(book.getPublisher());
        bookDTO.setPublicationDate(book.getPublicationDate());
        bookDTO.setCategory(book.getCategory());
        bookDTO.setSummary(book.getSummary());
        bookDTO.setContent(book.getContent());
        bookDTO.setCoverImageUrl(book.getCoverImageUrl());
        return bookDTO;
    }
    private Book convertToEntity(BookDTO bookDTO) {
        // 엔티티 변환 로직
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setCategory(bookDTO.getCategory());
        book.setSummary(bookDTO.getSummary());
        book.setContent(bookDTO.getContent());
        book.setCoverImageUrl(bookDTO.getCoverImageUrl());
        return book;
    }
}
