package BookMind.GraduationProject_BE.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {
    private Long id;

    @NotEmpty(message = "이메일은 필수입니다.")
    private String email;
    private String password;
}
