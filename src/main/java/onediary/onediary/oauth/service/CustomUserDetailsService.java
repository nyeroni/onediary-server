package onediary.onediary.oauth.service;

import lombok.RequiredArgsConstructor;
import onediary.onediary.domain.member.entity.Member;
import onediary.onediary.domain.member.repository.MemberQuerydslRepository;
import onediary.onediary.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberQuerydslRepository memberQuerydslRepository;

    @Override
    public UserDetails loadUserByUsername(String socialId) {
        Member member = memberQuerydslRepository.findBySocialId(socialId);
        if (member==null) {
            throw new UsernameNotFoundException("Can not find email.");
        }


        return User.builder()
                .username(member.getSocialId()) // 사용자의 식별자로 이메일 사용
                .password("") // 소셜 로그인에서는 사용하지 않으므로 빈 문자열로 설정
                .roles(member.getRole().name()) // 사용자의 역할(Role)
                .build();
    }
}