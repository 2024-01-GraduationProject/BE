package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.BookCategoryConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookCategoryConnectionRepository extends JpaRepository<BookCategoryConnection, Long> {
}
