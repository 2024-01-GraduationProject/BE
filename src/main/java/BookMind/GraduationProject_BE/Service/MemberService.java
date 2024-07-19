package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.DTO.InformationAndTasteDTO;
import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.Entity.Member;
import BookMind.GraduationProject_BE.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        member.setAgreements(memberDTO.getAgreements());
        memberRepository.save(member);
        return member.getEmail();
    }

    // 회원가입 중인 사용자에 연령, 성별 및 도서 취향 정보 추가 등록
    @Transactional
    public Member saveTaste(InformationAndTasteDTO informationAndTasteDTO) {
        List<Member> members = memberRepository.findByEmail(informationAndTasteDTO.getEmail());
        if (!members.isEmpty()) {
            Member member = members.get(0);
            memberRepository.saveInformationAndTaste(member, informationAndTasteDTO);
            memberRepository.save(member);
            return member;
        } else {
            throw new NoSuchElementException("존재하지 않는 회원입니다.");
        }
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
