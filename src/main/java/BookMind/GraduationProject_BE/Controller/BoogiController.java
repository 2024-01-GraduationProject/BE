package BookMind.GraduationProject_BE.Controller;

import BookMind.GraduationProject_BE.DTO.UserAnswerDTO;
import BookMind.GraduationProject_BE.Entity.UserAnswer;
import BookMind.GraduationProject_BE.Repository.UserAnswerRepository;
import BookMind.GraduationProject_BE.Service.GPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/boogi")
@RequiredArgsConstructor
public class BoogiController {

    private final GPTService gptService;
    private final UserAnswerRepository userAnswerRepository;

    // 질문 세션을 관리 (userId, 질문 수)
    private Map<Long, Integer> userQuestionCount = new HashMap<>();

    // 질문 중복 방지
    private Map<Long, Set<String>> userQuestions = new HashMap<>();

    // 책 제목을 기반으로 질문을 생성
    @GetMapping("/ask-question/{userId}/{bookTitle}")
    public ResponseEntity<String> askQuestion(@PathVariable Long userId, @PathVariable String bookTitle) {

        String congratsMessage = "부기: 축하해! 책 '" + bookTitle + "'을 완독했어! 첫 번째 질문을 줄게.";

        // 사용자 질문 세션 초기화
        userQuestionCount.put(userId, 1);
        userQuestions.put(userId, new HashSet<>());

        // 첫번째 질문 생성
        String firstQuestion = generateUniqueQuestion(userId, bookTitle);
        return ResponseEntity.ok(congratsMessage + "\n부기: " + firstQuestion);
    }

    // 사용자의 답변 받음, 추가 질문 받을지 선택
    @PostMapping("/answer")
    public ResponseEntity<String> handleAnswer(
            @RequestParam Long userId,
            @RequestBody String userAnswer,
            @RequestParam String question,
            @RequestParam String bookTitle,
            @RequestParam Long bookId) {
        // 답변 처리 + 필요시 DB에 저장
        UserAnswerDTO answerDTO = new UserAnswerDTO(userId, bookId, question, userAnswer, LocalDateTime.now().toString());
        saveAnswer(answerDTO);

        // 현재까지 질문한 갯수 가져옴
        int currentQuestionCount = userQuestionCount.getOrDefault(userId, 0);

        if (currentQuestionCount >= 3) {
            // 3개의 질문을 모두 받았으면 종료 메시지
            return ResponseEntity.ok("부기: 좋은 생각이야! 질문을 모두 마쳤어. 여기서 마치자.");
        } else {
            // 추가 질문을 받을지 선택
            String prompt = "부기: 좋은 생각이야! 다음 질문을 더 받을래? '예' 또는 '아니오'로 대답해줘.";
            return ResponseEntity.ok(prompt);
        }
    }

    // 추가적인 질문 처리
    @PostMapping("/next-question")
    public ResponseEntity<String> nextQuestion(
            @RequestParam Long userId,
            @RequestParam String response,
            @RequestParam String bookTitle) {

        // 사용자가 "YES"라고 답했을 경우
        if (response.equalsIgnoreCase("YES")) {
            int currentQuestionCount = userQuestionCount.getOrDefault(userId, 0);

            if (currentQuestionCount >= 3) {
                return ResponseEntity.ok("부기: 질문을 모두 마쳤어. 여기서 마치자.");
            }

            // 추가 질문 생성
            String nextQuestion = gptService.generateQuestion(bookTitle);
            userQuestionCount.put(userId, currentQuestionCount + 1);

            return ResponseEntity.ok("부기: " + nextQuestion);
        } else {
            // 사용자가 "NO"라고 답했을 경우
            return ResponseEntity.ok("부기: 그럼 여기서 대화를 마칠게! 다음에 또 보자.");
        }
    }

    // 사용자의 답변을 DB에 저장
    @PostMapping("/save-answer")
    public ResponseEntity<String> saveAnswer(@RequestBody UserAnswerDTO userAnswerDTO) {
        UserAnswer userAnswer = new UserAnswer(
                userAnswerDTO.getUserId(),
                userAnswerDTO.getBookId(),
                userAnswerDTO.getQuestion(),
                userAnswerDTO.getAnswer()
        );
        userAnswerRepository.save(userAnswer);

        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return ResponseEntity.ok("답변이 저장되었습니다. 저장 시각: " + createdAt);
    }

    // 사용자 답변 조회
    @GetMapping("/answers/{userId}/{bookId}")
    public ResponseEntity<List<UserAnswerDTO>> getAnswers(@PathVariable Long userId, @PathVariable Long bookId) {
        List<UserAnswer> answers = userAnswerRepository.findAllByUserIdAndBookId(userId, bookId);

        List<UserAnswerDTO> answerDTOs = answers.stream()
                .map(answer -> new UserAnswerDTO(
                        answer.getUserId(),
                        answer.getBookId(),
                        answer.getQuestion(),
                        answer.getAnswer(),
                        answer.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(answerDTOs);
    }

    // 중복되지 않는 질문을 생성하는 로직
    private String generateUniqueQuestion(Long userId, String bookTitle) {
        String question = gptService.generateQuestion(bookTitle);

        // 중복 확인 및 질문 재생성
        Set<String> userAskedQuestions = userQuestions.get(userId);
        while (userAskedQuestions.contains(question)) {
            question = gptService.generateQuestion(bookTitle);
        }

        // 질문 저장
        userAskedQuestions.add(question);
        userQuestions.put(userId, userAskedQuestions);

        return question;
    }
}
