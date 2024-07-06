package BookMind.GraduationProject_BE.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
// 이용약관 동의
public class Agreements {
    private boolean personalInfo; // 개인정보 동의
    private boolean eventAlarm; // 이벤트 알림 동의
}
