package BookMind.GraduationProject_BE.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userbook_indices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBookIndices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userbook_id", nullable = false)
    private UserBook userBook;

    @Column(name = "index_page")
    private float indexPage;

    public UserBookIndices(UserBook userBook, float indexPage) {
        this.userBook = userBook;
        this.indexPage = indexPage;
    }
}
