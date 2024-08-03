package BookMind.GraduationProject_BE.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category") // 테이블 이름이 "category"임을 명시
@Getter
@Setter
public class Category {

    @Id
    private String categoryId;  // PK

    private String category;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL) // Category와 BookTaste 간의 관계 설정
    private List<BookTaste> bookTaste = new ArrayList<>(); // 연결된 BookTaste 리스트
}


