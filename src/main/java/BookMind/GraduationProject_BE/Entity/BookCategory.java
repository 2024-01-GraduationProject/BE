package BookMind.GraduationProject_BE.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "BOOK_CATEGORY") // 테이블 이름이 "BOOK_CATEGORY"임을 명시
@Getter
@Setter
public class BookCategory {

    @Id
    private int category_id;  // PK

    private String category;

}


