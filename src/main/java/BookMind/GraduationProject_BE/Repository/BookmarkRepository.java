package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findAllByUserbookId(Long userbookId);
}
