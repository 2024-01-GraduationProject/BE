package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.DTO.InformationAndTasteDTO;
import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.DTO.UpdateMemberDTO;
import BookMind.GraduationProject_BE.Entity.Agreements;
import BookMind.GraduationProject_BE.Entity.Member;
import BookMind.GraduationProject_BE.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
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
        Optional<Member> members = memberRepository.findByEmail(informationAndTasteDTO.getEmail());
        if (members.isPresent()) {
            Member member = members.get();
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
        Optional<Member> findMembers = memberRepository.findByEmail(memberDTO.getEmail());
        if (findMembers.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 로그인
    public MemberDTO login(MemberDTO memberDTO) {
        /**
         * 1. 회원이 입력한 이메일을 DB에서 조회하여 회원 존재 여부 확인
         * 2. DB에서 조회한 회원의 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */
        // 이메일로 회원 조회 후 memberByEmail에 회원 정보 할당
        Optional<Member> memberByEmail = memberRepository.findByEmail(memberDTO.getEmail());
        // 회원 조회 -> 회원 존재
        if (memberByEmail.isPresent()) {
            Member member = memberByEmail.get(); // member에 조회된 회원 정보를 할당
            // 존재하는 회원과 사용자가 입력한 비밀번호가 일치하는지 확인
            if (member.getPassword().equals(memberDTO.getPassword())) {
                // 비밀번호 일치
                MemberDTO memDTO = toMemberDTO(member);
                return memDTO;
            } else {
                // 비밀번호 불일치
                System.out.println("가입된 비밀번호와 일치하지 않습니다.");
                return null;
            }
        } else {
            // 회원 조회 -> 해당 이메일을 가진 회원 없음
            System.out.println("가입되어 있지 않은 회원입니다.");
            return null;
        }
    }

//    // 로그인 (보안 및 가독성을 높인 버전 *** 추후 수정)
//    // 비밀번호 해싱 관련 사항 고려할 것
//    public MemberDTO login(MemberDTO memberDTO) {
//        // 이메일로 회원 조회 후 memberByEmail에 회원 정보 할당
//        Optional<Member> memberByEmail = memberRepository.findByEmail(memberDTO.getEmail());
//
//        // 회원 조회 -> 회원 존재
//        Member member = memberByEmail.orElseThrow(() -> new NoSuchElementException("가입되어 있지 않은 회원입니다."));
//
//        // 비밀번호 일치 여부 확인 (해싱된 비밀번호 비교)
//        // passwordEncoder는 Spring Security의 PasswordEncoder 인터페이스를 구현한 객체
//        if (passwordEncoder.matches(memberDTO.getPassword(), member.getPassword())) {
//            // 비밀번호 일치
//            return memberDTO;
//        } else {
//            // 비밀번호 불일치
//            throw new IllegalArgumentException("가입된 비밀번호와 일치하지 않습니다.");
//        }
//    }

    // 회원정보 업데이트
    public Member updateMember(Long userId, UpdateMemberDTO updateMemberDTO) {
        // DB에서 회원 정보 찾기
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        // DTO의 정보를 사용하여 회원 정보 업데이트
        member.setNickname(updateMemberDTO.getNewNickname());
        member.setEmail(updateMemberDTO.getNewEmail());
        member.setPassword(updateMemberDTO.getNewPassword());
        member.setAge(updateMemberDTO.getNewAge());
        member.setGender(updateMemberDTO.getNewGender());
        member.setMood(updateMemberDTO.getNewMood());

        // Agreements 중 EventAlarm만 업데이트
        Agreements agreements = member.getAgreements();
        agreements.setEventAlarm(updateMemberDTO.getAgreements().isEventAlarm()); // eventAlarm 값 업데이트
        member.setAgreements(agreements);

        // 업데이트된 회원 정보를 DB에 저장
        memberRepository.save(member);

        return member;
    }

    // member 객체를 memberDTO로 변환하는 메서드
    public MemberDTO toMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setEmail(member.getEmail());
        memberDTO.setPassword(member.getPassword());
        memberDTO.setNickname(member.getNickname());
        memberDTO.setAgreements(member.getAgreements());
        memberDTO.setAge(member.getAge());
        memberDTO.setGender(member.getGender());
        memberDTO.setMood(member.getMood());

        return memberDTO;
    }
}
