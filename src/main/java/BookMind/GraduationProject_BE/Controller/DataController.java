package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.CategoryDTO;
import BookMind.GraduationProject_BE.Entity.Age;
import BookMind.GraduationProject_BE.Entity.Category;
import BookMind.GraduationProject_BE.Entity.Gender;
import BookMind.GraduationProject_BE.Repository.AgeRepository;
import BookMind.GraduationProject_BE.Repository.CategoryRepository;
import BookMind.GraduationProject_BE.Repository.GenderRepository;
import BookMind.GraduationProject_BE.Service.DataPreprocessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DataController {

    @Autowired
    private final AgeRepository ageRepository;
    @Autowired
    private final GenderRepository genderRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final DataPreprocessingService dataPreprocessingService;

    // 연령 데이터를 조회
    @GetMapping("/ages")
    public ResponseEntity<List<Age>> getAllAges() {
        List<Age> ages = ageRepository.findAll();
        System.out.println("연령 데이터 불러오기 성공");
        return new ResponseEntity<>(ages, HttpStatus.OK);
    }
    // 성별 데이터를 조회
    @GetMapping("/genders")
    public ResponseEntity<List<Gender>> getAllGenders() {
        List<Gender> genders = genderRepository.findAll();
        System.out.println("성별 데이터 불러오기 성공");
        return new ResponseEntity<>(genders, HttpStatus.OK);
    }
    // 책 카테고리 데이터를 조회
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        // Category 객체를 CategoryDTO로 변환하여 반환
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> new CategoryDTO(category.getCategoryId(), category.getCategory()))
                .collect(Collectors.toList());

        System.out.println("책 카테고리 데이터 불러오기 성공");
        return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
    }

    // 추천시스템 전처리 데이터 > Flask 서버로 반환
    @GetMapping("/preprocess")
    public ResponseEntity<List<Map<String, Object>>> getPreprocessData() {
        List<Map<String, Object>> data = dataPreprocessingService.preprocessData();
        return ResponseEntity.ok(data); // JSON 형식으로 반환
    }
}
