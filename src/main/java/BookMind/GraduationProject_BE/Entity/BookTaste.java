package BookMind.GraduationProject_BE.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
//@Table(name = "book_taste")
@Getter
@Setter
public class BookTaste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bt_id; // pk

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id") // member와의 관계
    private Member member; // member와의 연관관계

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id") // category와의 관계
    private Category category; // category와의 연관관계
}
