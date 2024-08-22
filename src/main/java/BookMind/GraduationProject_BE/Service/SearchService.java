package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.DTO.BookDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    @Autowired
    private BookRepository bookRepository;

    public List<BookDTO> searchBooks(Object searchWord) {
        String searchKeyword = searchWord.toString();

        // 검색어를 포함한 도서 목록 조회
        List<Book> books = bookRepository.findByTitleContainingOrAuthorContainingOrPublisherContainingOrCategoryContainingOrSummaryContainingOrPublicationDateContaining(
                searchKeyword, searchKeyword, searchKeyword, searchKeyword, searchKeyword, searchKeyword);

        // Book 엔티티를 BookDTO로 변환
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Book 엔티티 >> BookDTO로 변환하는 함수
    private BookDTO convertToDTO(Book book) {
        // DTO 변환 로직
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setPublisher(book.getPublisher());
        bookDTO.setCategory(book.getCategory());
        bookDTO.setSummary(book.getSummary());
        bookDTO.setContent(book.getContent());
        bookDTO.setCoverImageUrl(book.getCoverImageUrl());
        bookDTO.setPublicationDate(book.getPublicationDate());
        return bookDTO;
    }
}
