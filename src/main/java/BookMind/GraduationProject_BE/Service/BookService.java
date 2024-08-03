package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.DTO.BookDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

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

    @Transactional
    public BookDTO saveBook(BookDTO bookDTO){
        Book book = convertToEntity(bookDTO);
        book = bookRepository.save(book);
        return convertToDTO(book);
    }

    @Transactional
    public BookDTO updateBook(BookDTO bookDTO){
        Optional<Book> book = bookRepository.findById(bookDTO.getId());
        if (book.isPresent()) {
            Book updatedBook = convertToEntity(bookDTO);
            return convertToDTO(bookRepository.save(updatedBook));
        } else {
            throw new NoSuchElementException("No book found with the given ID.");
        }
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
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
        book.setCoverImageUrl(bookDTO.getCoverImageUrl());
        return book;
    }
}
