package BookMind.GraduationProject_BE.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "GENDER") // 테이블 이름이 "GENDER"임을 명시
@Getter
@Setter
public class Gender {

    @Id
    private int id;  // PK

    private String gender;
}
