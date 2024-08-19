package BookMind.GraduationProject_BE.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookCategoryConnection {

    @Id
    private Long bcId;

    private Long bookId;
    private String categoryId;

}
