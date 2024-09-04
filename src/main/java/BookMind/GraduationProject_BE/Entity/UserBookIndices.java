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

    @Column(name = "userbook_id")
    private String userbookId;

    @Column(name = "index_page")
    private float indexPage;

    public UserBookIndices(String userbookId, float indexPage) {
        this.userbookId = userbookId;
        this.indexPage = indexPage;
    }
}
