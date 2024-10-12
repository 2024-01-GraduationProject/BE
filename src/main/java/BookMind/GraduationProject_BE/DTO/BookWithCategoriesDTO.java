package BookMind.GraduationProject_BE.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data

public class BookWithCategoriesDTO {
    private BookDTO book;
    private List<String> bookCategory;

    // 생성자
    public BookWithCategoriesDTO(BookDTO book, List<String> bookCategory) {
        this.book = book;
        this.bookCategory = bookCategory;
    }
}

