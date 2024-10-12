package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    // 카테고리 이름으로 Category 객체를 조회하는 메소드
    Category findByCategory(String name);

    // 여러 개의 카테고리 ID로 카테고리 이름을 조회
    @Query("SELECT c.category FROM Category c WHERE c.categoryId IN :categoryIds")
    List<String> findCategoryNamesByCategoryIdIn(@Param("categoryIds") List<String> categoryIds);
}
