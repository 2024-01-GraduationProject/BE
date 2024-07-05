package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.Entity.Member;
import BookMind.GraduationProject_BE.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 메서드들이 읽기 전용 트랜잭션으로 실행됨
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public String join(MemberDTO memberDTO) {
        validateDuplicateMember(memberDTO); // 중복 회원 검사

        // MemberDTO를 Member로 변환
        Member member = new Member();
        member.setEmail(memberDTO.getEmail());
        member.setPassword(memberDTO.getPassword());
        member.setNickname(memberDTO.getNickname());
        memberRepository.save(member);
        return member.getEmail();
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 이메일을 통해 중복 회원 검사
    private void validateDuplicateMember(MemberDTO memberDTO) {
        List<Member> findMembers = memberRepository.findByEmail(memberDTO.getEmail());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
