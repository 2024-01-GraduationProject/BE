package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.Repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 테스트 실행 후 롤백, DB에 관련 데이터가 남지 않음.
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    // 회원 가입 테스트
    @Test
    public void 회원가입() throws Exception {
        //given
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail("lsh21@sookmyung.ac.kr");
        memberDTO.setPassword("333aaaa!");
        memberDTO.setNickname("임서현");
        memberDTO.setAgreements("agree");

        //when
        String savedEmail = memberService.join(memberDTO);

        //then
        em.flush(); // 영속성 컨텍스트내의 변경 사항을 데이터베이스에 반영하는 역할
        System.out.println("memberDTO = " + memberDTO);
        assertNotNull(savedEmail);
        // 실제 DB에 저장된 회원 이메일과 테스트에서 사용한 MemberDTO의 이메일이 일치하는지 확인
        assertEquals(memberDTO.getEmail(), savedEmail);
    }

//    // 중복 회원 가입 테스트 ***** 데이터무결성 예외가 발생 (확인 필요)
//    @Test(expected = IllegalStateException.class)
//    public void 중복_회원_가입() throws Exception {
//        //given
//        MemberDTO member1 = new MemberDTO();
//        member1.setEmail("lsh21@sookmyung.ac.kr");
//
//        MemberDTO member2 = new MemberDTO();
//        member2.setEmail("lsh21@sookmyung.ac.kr");
//
//        //when
//        memberService.join(member1);
//        memberService.join(member2); // 예외가 발생해야 한다!!!
//
//        //then
//        fail("예외가 발생해야 한다.");
//    }
}