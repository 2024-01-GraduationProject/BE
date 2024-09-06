package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.UserBook;
import BookMind.GraduationProject_BE.Entity.UserBookIndices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBookIndicesRepository extends JpaRepository<UserBookIndices, Long> {

    // userbook에 해당하는 모든 인덱스 조회
    List<UserBookIndices> findAllByUserBook(UserBook userBook);

    // userbook과 indexPage에 해당하는 인덱스가 존재하는지 확인
    boolean existsByUserBookAndIndexPage(UserBook userBook, Float indexPage);

}
