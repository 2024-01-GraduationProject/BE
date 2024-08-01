package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    // 카테고리 이름으로 Category 객체를 조회하는 메소드
    Category findByName(String name);
}
