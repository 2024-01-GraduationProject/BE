package BookMind.GraduationProject_BE.Service;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;




@Service
public class GPTService {

    @Value("${openai.api.key}")
    public String openaiApiKey;

    public String generateQuestion(String bookTitle) {
        try {
            // OpenAI API로 요청 보내기
            HttpResponse<JsonNode> response = Unirest.post("https://api.openai.com/v1/completions")
                    .header("Authorization", "Bearer " + openaiApiKey)
                    .header("Content-Type", "application/json")
                    .body(new JSONObject()
                            .put("model", "gpt-3.5-turbo")
                            .put("prompt", "책 '" + bookTitle + "'에 대해 독자가 생각해볼만한 질문을 하나만 한국어로 생성해줘.")
                            .put("max_tokens", 100)
                            .put("temperature", 0.7)
                            .put("n", 1)
                            .toString())
                    .asJson();

            // 응답 로그 출력
            System.out.println("API 응답: " + response.getBody().toString());

            // 상태 코드 확인
            if (response.getStatus() != 200) {
                System.out.println("Error response: " + response.getBody().toString());
                return "질문을 생성하는 중 오류가 발생했습니다.";
            }

            // 응답에서 질문 추출
            String gptResponse = response.getBody().getObject()
                    .getJSONArray("choices").getJSONObject(0).getString("text");

            // 질문 확인용 출력
            System.out.println("받아온 질문: " + gptResponse);

            return gptResponse.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "질문을 생성하는 중 오류가 발생했습니다.";
        }
    }
}
