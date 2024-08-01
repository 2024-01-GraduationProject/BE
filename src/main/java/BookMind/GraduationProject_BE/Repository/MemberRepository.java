package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.DTO.InformationAndTasteDTO;
import BookMind.GraduationProject_BE.Entity.BookTaste;
import BookMind.GraduationProject_BE.Entity.Category;
import BookMind.GraduationProject_BE.Entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor // 생성자를 자동으로 생성해 줌 (의존성 주입 간편)
// * 리팩토링 시 고려사항: JpaRepository를 상속받아서 class가 아닌 interface로 전환 고려해볼 것
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    private final CategoryRepository categoryRepository;

    // 회원 등록
    public void save(Member member) {
        em.persist(member);
    }

    // 회원 정보 추가 등록 (연령, 성별, 도서 취향)
    public void saveInformationAndTaste(Member member, InformationAndTasteDTO informationAndTasteDTO) {
        // Member 엔티티에 InformationAndTasteDTO 정보 설정
        member.setAge(informationAndTasteDTO.getAge()); // 연령
        member.setGender(informationAndTasteDTO.getGender()); // 성별

        // BookTaste 정보를 Member 엔티티에 설정
        List<String> bookTasteNames = informationAndTasteDTO.getBookTaste();
        List<BookTaste> bookTasteList = new ArrayList<>();

        for (String tasteName : bookTasteNames) {
            // 각 카테고리 이름에 해당하는 Category 객체를 찾아야 함
            Category category = categoryRepository.findByCategory(tasteName); // 카테고리 이름으로 조회

            if (category != null) {
                BookTaste bookTaste = new BookTaste();
                bookTaste.setMember(member); // Member와의 관계 설정
                bookTaste.setCategory(category); // Category와의 관계 설정
                bookTasteList.add(bookTaste);
            }
        }

        member.setBookTaste(bookTasteList); // 도서 취향

        em.persist(member);
    }

    // 회원 조회 _ 특정 아이디를 갖는 Member 엔티티 조회
    public Optional<Member> findByUserId(Long userId) {
        List<Member> members = em.createQuery("select m from Member m where m.userId = :userId", Member.class)
                .setParameter("userId", userId)
                .getResultList();

        return members.stream().findFirst();
    }

    // 회원 조회 _ 특정 이메일을 갖는 Member 엔티티 조회
    public Optional<Member> findByEmail(String email) {
        List<Member> members = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return members.stream().findFirst(); // 리스트에서 첫 번째 요소를 Optional로 반환
    }

    // 회원 조회 _ 특정 닉네임을 갖는 Member 엔티티 조회
    public Optional<Member> findByNickname(String nickname) {
        List<Member> members = em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();

        return members.stream().findFirst();
    }

    // 회원 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
