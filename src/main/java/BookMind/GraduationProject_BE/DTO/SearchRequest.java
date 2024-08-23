package BookMind.GraduationProject_BE.DTO;

import lombok.Data;

@Data
public class SearchRequest {

    // 검색어의 데이터 타입 상관없이 전달받을 수 있도록 Object로 설정
    private Object searchWord;
}
