package onediary.onediary.oauth.service;

import lombok.RequiredArgsConstructor;
import onediary.onediary.api.domain.member.entity.Member;
import onediary.onediary.api.domain.member.repository.MemberRepository;
import onediary.onediary.oauth.entity.MemberPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        Member member = memberRepository.findByUserId(username);
        if(member==null){
            throw new UsernameNotFoundException("Can not find username.");

        }
        return MemberPrincipal.create(member);
    }

}
