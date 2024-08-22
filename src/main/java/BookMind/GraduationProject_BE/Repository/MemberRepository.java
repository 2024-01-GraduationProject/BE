package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.DTO.InformationAndTasteDTO;
import BookMind.GraduationProject_BE.Entity.BookTaste;
import BookMind.GraduationProject_BE.Entity.Category;
import BookMind.GraduationProject_BE.Entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 회원 조회 _ 특정 아이디를 갖는 Member 엔티티 조회
    Optional<Member> findByUserId(Long userId);

    // 회원 조회 _ 특정 이메일을 갖는 Member 엔티티 조회
    Optional<Member> findByEmail(String email);

    // 회원 조회 _ 특정 닉네임을 갖는 Member 엔티티 조회
    Optional<Member> findByNickname(String nickname);
}
