package BookMind.GraduationProject_BE.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.SpringVersion;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class InformationAndTasteDTO {

    private String email; // 회원가입 중인 사용자의 이메일을 받아옴
    // 연령, 성별, 도서 취향
    private String age;
    private String gender;
    private List<String> mood;
}
