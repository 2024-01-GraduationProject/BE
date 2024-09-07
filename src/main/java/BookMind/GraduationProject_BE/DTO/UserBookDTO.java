package BookMind.GraduationProject_BE.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserBookDTO {

    private String userbookId;
    private Long bookId;
    private String title;
    private String author;
    private String category;

    // UserBook과 관련된 필드들
    private String status; // 읽기 상태 (READING, COMPLETED)
    private Boolean favorite; // 즐겨찾기 여부
    private float lastReadPage; // 마지막으로 읽은 페이지 (진도율)
    private String startDate; // 시작일
    private String endDate; // 완료일
    private Byte rating; // 평점
    private List<Float> indexPages; // 인덱스 리스트

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
