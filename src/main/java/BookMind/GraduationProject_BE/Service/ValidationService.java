package BookMind.GraduationProject_BE.Service;

import BookMind.GraduationProject_BE.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    @Autowired
    private MemberRepository memberRepository;

    public boolean isDuplicateEmail(String email) {
        return !memberRepository.findByEmail(email).isEmpty();
    }
}
