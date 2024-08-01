package BookMind.GraduationProject_BE.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter  // ***** setter는 닫아두는 것이 좋음. 하지만 개발 중에는 열어둠.
public class Member {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // id값이 DB에 저장될 때 자동으로 증가하면서 생성
    private Long userId;

    @Column(nullable = false, unique = true)
    // email에 해당하는 컬럼은 NULL 값을 가질 수 없고, 각 값이 유일하도록 설정
    private String email;

    // *****
    // 1) @Pattern으로 비밀번호 유효성 검사 가능
    // 2) @Valid 어노테이션을 사용하여 Controller나 Service 레벨에서 검증을 수행 가능
    @Column(nullable = false)
    // NULL 값이 존재하지 않도록 설정
    private String password;

    private String nickname;

    @Embedded //Agreements 클래스의 필드들이 Member 엔티티의 컬럼으로 저장
    private Agreements agreements;

    // 사용자의 선택에 따라 새롭게 추가되는 정보들
    // 연령, 성별, 책 취향 정보
    private String age;

    private String gender;

//    @ElementCollection
//    @CollectionTable(name = "book_taste", joinColumns = @JoinColumn(name = "user_id"))
//    @Column(name = "taste")
//    private List<String> mood;

    // 위에 mood 지우고 아래 주석으로 바뀌어야 함. *****
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // Member와 BookTaste 간의 관계 설정
    private List<BookTaste> bookTaste = new ArrayList<>(); // BookTaste 리스트

    // 로그인 방식
    private String loginMethod;

    // 계정 생성 날짜 및 업데이트 날짜 (추가하기) *****
//    private LocalDateTime createdAt;

//    private LocalDateTime updatedAt;
}
