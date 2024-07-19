package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.DTO.InformationAndTasteDTO;
import BookMind.GraduationProject_BE.Entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor // 생성자를 자동으로 생성해 줌 (의존성 주입 간편)
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    // 회원 등록
    public void save(Member member) {
        em.persist(member);
    }

    // 회원 정보 추가 등록 (연령, 성별, 도서 취향)
    public void saveInformationAndTaste(Member member, InformationAndTasteDTO informationAndTasteDTO) {
        // Member 엔티티에 InformationAndTasteDTO 정보 설정
        member.setAge(informationAndTasteDTO.getAge());
        member.setGender(informationAndTasteDTO.getGender());
        member.setMood(informationAndTasteDTO.getMood());

        em.persist(member);
    }

    // 회원 조회 _ 특정 이메일을 갖는 Member 엔티티 조회
    public List<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }

    // 회원 조회 _ 특정 닉네임을 갖는 Member 엔티티 조회
    public List<Member> findByNickname(String nickname) {
        return  em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }

    // 회원 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

//    // 회원 조회 _ 특정 나이, 성별, 기분을 갖는 Member 엔티티 조회
//    public List<Member> findByInformationAndTaste(String selectedAge, String selectedGender, String selectedMoods) {
//        return em.createQuery("select m from Member m where m.selectedAge = :selectedAge and m.selectedGender = :selectedGender and m.selectedMoods = :selectedMoods", Member.class)
//                .setParameter("selectedAge", selectedAge)
//                .setParameter("selectedGender", selectedGender)
//                .setParameter("selectedMoods", selectedMoods)
//                .getResultList();
//    }

}
