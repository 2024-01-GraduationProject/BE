package BookMind.GraduationProject_BE.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_id; //PK

    private String title;
    private String author;
    private String publisher;
    private String publicationDate;
    private String category;
    private String summary;
    private String content;
    private String coverImageUrl;

    public Long getId(){
        return book_id;
    }

    public void setId(Long book_id){
        this.book_id = book_id;
    }

}
