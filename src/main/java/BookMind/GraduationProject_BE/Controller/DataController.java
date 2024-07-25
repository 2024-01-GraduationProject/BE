package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.Entity.Age;
import BookMind.GraduationProject_BE.Entity.BookCategory;
import BookMind.GraduationProject_BE.Entity.Gender;
import BookMind.GraduationProject_BE.Repository.AgeRepository;
import BookMind.GraduationProject_BE.Repository.BookCategoryRepository;
import BookMind.GraduationProject_BE.Repository.GenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataController {

    @Autowired
    private final AgeRepository ageRepository;
    @Autowired
    private final GenderRepository genderRepository;
    @Autowired
    private final BookCategoryRepository bookCategoryRepository;

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
    @GetMapping("/book-categories")
    public ResponseEntity<List<BookCategory>> getAllBookCategories() {
        List<BookCategory> bookCategories = bookCategoryRepository.findAll();
        System.out.println("책 카테고리 데이터 불러오기 성공");
        return new ResponseEntity<>(bookCategories, HttpStatus.OK);
    }

}
