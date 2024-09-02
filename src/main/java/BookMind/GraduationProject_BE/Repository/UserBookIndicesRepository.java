package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.UserBookIndices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBookIndicesRepository extends JpaRepository<UserBookIndices, Long> {

    // userbookId에 해당하는 모든 인덱스 조회
    List<UserBookIndices> findAllByUserbookId(String userbookId);

    // userbookId와 indexPage에 해당하는 인덱스가 존재하는지 확인
    boolean existsByUserbookIdAndIndexPage(String userbookId, float indexPage);
}
