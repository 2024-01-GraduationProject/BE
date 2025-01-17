package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.DTO.InformationAndTasteDTO;
import BookMind.GraduationProject_BE.DTO.MemberDTO;
import BookMind.GraduationProject_BE.DTO.UpdateMemberDTO;
import BookMind.GraduationProject_BE.Entity.Agreements;
import BookMind.GraduationProject_BE.Entity.BookTaste;
import BookMind.GraduationProject_BE.Entity.Category;
import BookMind.GraduationProject_BE.Entity.Member;
import BookMind.GraduationProject_BE.Repository.BookTasteRepository;
import BookMind.GraduationProject_BE.Repository.CategoryRepository;
import BookMind.GraduationProject_BE.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // 메서드들이 읽기 전용 트랜잭션으로 실행됨
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final BookTasteRepository bookTasteRepository;

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
        member.setLoginMethod("BookMind 회원");
        memberRepository.save(member);
        return member.getEmail();
    }

    // 회원가입 중인 사용자에 연령, 성별 및 도서 취향 정보 추가 등록
    @Transactional
    public Member saveTaste(InformationAndTasteDTO informationAndTasteDTO) {
        Optional<Member> memberOptional = memberRepository.findByEmail(informationAndTasteDTO.getEmail());
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            // Member 엔티티에 InformationAndTasteDTO 정보 설정
            member.setAge(informationAndTasteDTO.getAge()); // 연령 설정
            member.setGender(informationAndTasteDTO.getGender()); // 성별 설정

            // BookTaste 정보를 Member 엔티티에 설정
            List<String> bookTasteNames = informationAndTasteDTO.getBookTaste();
            List<BookTaste> bookTasteList = new ArrayList<>();

            // 각 카테고리 이름에 해당하는 Category 객체를 찾아야 함
            for (String tasteName : bookTasteNames) {
                Category category = categoryRepository.findByCategory(tasteName); // 카테고리 이름으로 조회
                if (category != null) {
                    BookTaste bookTaste = new BookTaste();
                    bookTaste.setMember(member); // Member와의 관계 설정
                    bookTaste.setCategory(category); // Category와의 관계 설정
                    bookTasteList.add(bookTaste);
                }
            }

            member.setBookTaste(bookTasteList); // 도서 취향 설정

            // 변경된 회원 정보 저장
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
//        // 이메일로 회원 조회 후 memberByEmail에 회원 정보 할당
//
//        // 회원 조회 -> 회원 존재
//        // 비밀번호 일치 여부 확인 (해싱된 비밀번호 비교)
//        // passwordEncoder는 Spring Security의 PasswordEncoder 인터페이스를 구현한 객체


    // 회원정보 업데이트
    @Transactional // 해당 어노테이션이 없으면 읽기 전용으로 처리되기 때문에 수정되지 않음.
    public Member updateMember(Long userId, UpdateMemberDTO updateMemberDTO) {
        // DB에서 회원 정보 찾기
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        // DTO의 정보를 사용하여 회원 정보 업데이트
        // 해당 값의 null 체크를 통해 비어있으면 업데이트 하지 않음.
        if (updateMemberDTO.getNewNickname() != null && !updateMemberDTO.getNewNickname().isEmpty()) {
            member.setNickname(updateMemberDTO.getNewNickname());
        }

        if (updateMemberDTO.getNewPassword() != null && !updateMemberDTO.getNewPassword().isEmpty()) {
            member.setPassword(updateMemberDTO.getNewPassword());
        }

        if (updateMemberDTO.getNewAge() != null) {
            member.setAge(updateMemberDTO.getNewAge());
        }

        if (updateMemberDTO.getNewGender() != null) {
            member.setGender(updateMemberDTO.getNewGender());
        }

        // BookTaste 업데이트
        if (updateMemberDTO.getNewBookTaste() != null) {
            // 기존 BookTaste 삭제
            bookTasteRepository.deleteByMember(member); // 사용자의 BookTaste 삭제

            List<String> newBookTasteNames = updateMemberDTO.getNewBookTaste();
            List<BookTaste> newBookTasteList = new ArrayList<>();

            for (String tasteName : newBookTasteNames) {
                Category category = categoryRepository.findByCategory(tasteName); // 카테고리 이름으로 조회

                if (category != null) {
                    BookTaste newBookTaste = new BookTaste();
                    newBookTaste.setMember(member); // Member 설정
                    newBookTaste.setCategory(category); // Category 설정
                    newBookTasteList.add(newBookTaste);
                }
            }

            // 새로운 BookTaste 리스트 저장
            member.setBookTaste(newBookTasteList); // Member 엔티티에 새로운 BookTaste 리스트 설정
        }

        // Agreements 중 EventAlarm만 업데이트
        Agreements agreements = member.getAgreements();
        if (updateMemberDTO.getAgreements() != null &&
                updateMemberDTO.getAgreements().isEventAlarm() != agreements.isEventAlarm()) {
            agreements.setEventAlarm(updateMemberDTO.getAgreements().isEventAlarm()); // eventAlarm 값 업데이트
            member.setAgreements(agreements);
        }

        // 업데이트된 회원 정보를 DB에 저장
        memberRepository.save(member);

        return member;
    }


    // member 객체를 memberDTO로 변환하는 메서드
    public MemberDTO toMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setUserId(member.getUserId());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPassword(member.getPassword());
        memberDTO.setNickname(member.getNickname());
        memberDTO.setAgreements(member.getAgreements());
        memberDTO.setAge(member.getAge());
        memberDTO.setGender(member.getGender());
        // BookTaste 변환
        if (member.getBookTaste() != null) {
            List<String> bookTasteNames = member.getBookTaste().stream()
                    .map(bookTaste -> bookTaste.getCategory().getCategory()) // 카테고리 이름 추출
                    .collect(Collectors.toList());
            memberDTO.setBookTaste(bookTasteNames);
        }
        memberDTO.setLoginMethod(member.getLoginMethod());

        return memberDTO;
    }
}
