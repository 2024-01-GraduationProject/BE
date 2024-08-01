package BookMind.GraduationProject_BE.DTO;

import BookMind.GraduationProject_BE.Entity.Agreements;
import BookMind.GraduationProject_BE.Entity.BookTaste;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {

    private Long userId;
//    @NotEmpty(message = "이메일은 필수입니다.")
    private String email;
    private String password;
    private String nickname;
    private Agreements agreements;
    // 추가적으로 등록한 사용자 정보
    private String age;
    private String gender;
    private List<String> bookTaste;
    // 로그인 방식
    private String loginMethod;
}
