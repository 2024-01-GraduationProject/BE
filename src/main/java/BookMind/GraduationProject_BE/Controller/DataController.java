package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.CategoryDTO;
import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.Entity.Age;
import BookMind.GraduationProject_BE.Entity.Category;
import BookMind.GraduationProject_BE.Entity.Gender;
import BookMind.GraduationProject_BE.Repository.AgeRepository;
import BookMind.GraduationProject_BE.Repository.CategoryRepository;
import BookMind.GraduationProject_BE.Repository.GenderRepository;
import BookMind.GraduationProject_BE.Service.DataPreprocessingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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
}
