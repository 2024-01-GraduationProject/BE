package BookMind.GraduationProject_BE.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book")
@Getter
@Setter

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //PK

    private String title;
    private String author;
    private String publisher;
    private String publicationDate;
    //private String genre;
    private String summary;
    private String coverImageUrl;

}
