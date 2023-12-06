package onediary.onediary.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import onediary.onediary.domain.member.entity.Role;
import onediary.onediary.dto.member.MemberResponseDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@AllArgsConstructor
public class MemberPrincipal implements UserDetails {
    private String socialId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorites;

    @Setter
    private Map<String, Objects> attributes;

    public static MemberPrincipal create(MemberResponseDto memberResponseDto){
        List<GrantedAuthority> authorites = Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getCode()));
        return new MemberPrincipal(
                memberResponseDto.getSocialId(),
                memberResponseDto.getEmail(),
                "",
                authorites,
                null
        );
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
