package BookMind.GraduationProject_BE.DTO;

import BookMind.GraduationProject_BE.Entity.Agreements;
import BookMind.GraduationProject_BE.Entity.BookTaste;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateMemberDTO {

    private String newNickname;
//    private String newEmail;
    private String newPassword;
    private String newAge;
    private String newGender;
    private List<String> newBookTaste;
    private Agreements agreements;

}
