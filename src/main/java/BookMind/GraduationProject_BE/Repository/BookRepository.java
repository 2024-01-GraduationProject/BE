package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN BookCategoryConnection bcc ON b.bookId = bcc.bookId " +
            "JOIN Category c ON bcc.categoryId = c.categoryId " +
            "WHERE c.category = :categoryName")
    List<Book> findBooksByCategoryName(@Param("categoryName") String categoryName);

    // 도서 제목, 저자, 출판사, 카테고리, 줄거리, 출판날짜로 도서 조회하기
    List<Book> findByTitleContainingOrAuthorContainingOrPublisherContainingOrCategoryContainingOrSummaryContainingOrPublicationDateContaining(
            String title, String author, String publisher, String category, String summary, String publicaionDate);

    // 여러 개의 bookIds를 통해서 Book 조회
    List<Book> findByBookIdIn(List<Long> bookIds);
}
