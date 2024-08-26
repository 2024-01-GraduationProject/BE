package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.Entity.BookCategoryConnection;
import BookMind.GraduationProject_BE.Entity.BookTaste;
import BookMind.GraduationProject_BE.Entity.UserBook;
import BookMind.GraduationProject_BE.Repository.BookCategoryConnectionRepository;
import BookMind.GraduationProject_BE.Repository.BookTasteRepository;
import BookMind.GraduationProject_BE.Repository.UserBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataPreprocessingService {

    @Autowired
    private UserBookRepository userBookRepository;
    @Autowired
    private BookCategoryConnectionRepository bookCategoryConnectionRepository;
    @Autowired
    private BookTasteRepository bookTasteRepository;

    public List<Map<String, Object>> preprocessData() {
        // 1단계: userBook과 bookCategoryConnection 병합하여 category_id 찾기
        List<UserBook> userBooks = userBookRepository.findAll();
        List<BookCategoryConnection> bookCategories = bookCategoryConnectionRepository.findAll();

        Map<Long, List<String>> userCategoryMap = new HashMap<>();

        for (UserBook userBook : userBooks) {
            Long bookId = userBook.getBookId();
            List<String> categoryIds = bookCategories.stream()
                    .filter(bc -> bc.getBookId().equals(bookId))
                    .map(BookCategoryConnection::getCategoryId)
                    .collect(Collectors.toList());

            userCategoryMap.computeIfAbsent(userBook.getUserId(), k -> new ArrayList<>()).addAll(categoryIds);
        }

        // 2단계: bookTaste와 병합
        List<BookTaste> bookTastes = bookTasteRepository.findAll();
        for (BookTaste bookTaste : bookTastes) {
            Long userId = bookTaste.getMember().getUserId(); // Member 엔티티에서 userId 가져오기
            String categoryId = bookTaste.getCategory().getCategoryId(); // Category 엔티티에서 categoryId 가져오기

            userCategoryMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(categoryId);
        }

        // 3단계: 카테고리의 고유한 인덱스 생성
        Set<String> uniqueCategories = userCategoryMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        Map<String, Integer> categoryIndexMap = new HashMap<>();
        int index = 0;
        for (String category : uniqueCategories) {
            categoryIndexMap.put(category, index++);
        }

        // 4단계: 원-핫 인코딩 적용
        List<Map<String, Object>> finalResult = new ArrayList<>();
        for (Map.Entry<Long, List<String>> entry : userCategoryMap.entrySet()) {
            Long userId = entry.getKey();
            List<String> combinedCategoryIds = new ArrayList<>(new HashSet<>(entry.getValue())); // 중복 제거

            // 원-핫 인코딩 벡터 생성
            int[] oneHotVector = new int[categoryIndexMap.size()];
            for (String categoryId : combinedCategoryIds) {
                Integer categoryIdx = categoryIndexMap.get(categoryId);
                if (categoryIdx != null) {
                    oneHotVector[categoryIdx] = 1;
                }
            }

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("user_id", userId);
            resultMap.put("one_hot_encoding", oneHotVector);
            finalResult.add(resultMap);
        }

        return finalResult;
    }
}
