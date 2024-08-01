package BookMind.GraduationProject_BE.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Data;

@Data // @Getter & @Setter 포함
public class CategoryDTO {

    private String categoryId;
    private String category;

    // 커스텀 생성자
    public CategoryDTO(String categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
    }
}

