package BookMind.GraduationProject_BE.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter  // ***** setter는 닫아두는 것이 좋음. 하지만 개발 중에는 열어둠.
public class Member {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // id값이 DB에 저장될 때 자동으로 증가하면서 생성
    private Long id;

    @Column(nullable = false, unique = true)
    // email에 해당하는 컬럼은 NULL 값을 가질 수 없고, 각 값이 유일하도록 설정
    private String email;

    // *****
    // 1) @Pattern으로 비밀번호 유효성 검사 가능
    // 2) @Valid 어노테이션을 사용하여 Controller나 Service 레벨에서 검증을 수행 가능
    @Column(nullable = false)
    // NULL 값이 존재하지 않도록 설정
    private String password;
}
