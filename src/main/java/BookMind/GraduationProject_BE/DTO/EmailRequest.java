package BookMind.GraduationProject_BE.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data // @Getter & @Setter 포함
@NoArgsConstructor
public class EmailRequest {
    private String email;
}
