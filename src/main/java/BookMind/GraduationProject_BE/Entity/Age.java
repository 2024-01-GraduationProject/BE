package BookMind.GraduationProject_BE.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "AGE") // 테이블 이름이 "AGE"임을 명시
@Getter
@Setter
public class Age {

    @Id
    private int id;  // PK

    private String age;
}
