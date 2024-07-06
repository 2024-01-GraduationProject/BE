package BookMind.GraduationProject_BE.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data // @Getter & @Setter 포함
@NoArgsConstructor
public class EmailRequest {
    private String email;
}
