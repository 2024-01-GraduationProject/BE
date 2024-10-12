package BookMind.GraduationProject_BE.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MonthlyReadingDTO {
    private int currentMonth;
    private int readingCount;


    // 생성자
    // 생성자 없으면 Controller에서 값 세팅할 떄 에러 남.
    public MonthlyReadingDTO(int currentMonth, int readingCount) {
        this.currentMonth = currentMonth;
        this.readingCount = readingCount;
    }
}
