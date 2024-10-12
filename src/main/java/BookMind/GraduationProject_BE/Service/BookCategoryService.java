package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.Repository.BookCategoryConnectionRepository;
import BookMind.GraduationProject_BE.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoryService {

    @Autowired
    private final BookCategoryConnectionRepository bookCategoryConnectionRepository;
    @Autowired
    private final CategoryRepository categoryRepository;

    public List<String> getCategoryName(Long BookId) {
        // BCC 테이블에서 해당 책의 category 식별코드를 받아옴.
        List<String> categoryIds = bookCategoryConnectionRepository.findCategoryIdsByBookId(BookId);
        // Category 테이블에서 categoryIds의 한글명을 받아옴.
        List<String> categoryNames = categoryRepository.findCategoryNamesByCategoryIdIn(categoryIds);

        return categoryNames;
    }
}
