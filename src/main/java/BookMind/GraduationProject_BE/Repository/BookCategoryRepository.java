package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, String> {
}
