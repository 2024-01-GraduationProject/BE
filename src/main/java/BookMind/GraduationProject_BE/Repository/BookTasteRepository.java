package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.BookTaste;
import BookMind.GraduationProject_BE.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookTasteRepository  extends JpaRepository<BookTaste, Long> {
    void deleteByMember(Member member); // Member 객체로 BookTaste 삭제
}
