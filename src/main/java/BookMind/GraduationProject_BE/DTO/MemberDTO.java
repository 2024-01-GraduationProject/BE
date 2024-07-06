package BookMind.GraduationProject_BE.DTO;

import BookMind.GraduationProject_BE.Entity.Agreements;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {
    //private Long id;

//    @NotEmpty(message = "이메일은 필수입니다.")
    private String email;
    private String password;
    private String nickname;
    private Agreements agreements;
}
