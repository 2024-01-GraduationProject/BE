package BookMind.GraduationProject_BE.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BookDTO {
    private Long book_id;
    private String title;
    private String author;
    private String publisher;
    private String publicationDate;
    //private String genre;
    private String summary;
    private String coverImageUrl;


    public Long getId(){
        return book_id;
    }

    public void setId(Long book_id){
        this.book_id = book_id;
    }
}
