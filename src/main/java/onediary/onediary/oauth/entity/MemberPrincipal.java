package onediary.onediary.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import onediary.onediary.api.domain.member.entity.Member;
import onediary.onediary.api.domain.member.entity.Role;
import onediary.onediary.api.domain.member.entity.SocialProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberPrincipal implements OAuth2User, UserDetails, OidcUser {
    private final String userId;
    private final String password;
    private final SocialProvider socialProvider;
    private final Role role;
    private final Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;


    @Override
    public Map<String, Object> getAttributes(){
        return attributes;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;

    }
    @Override
    public String getName() {
        return userId;
    }

    @Override
    public String getUsername(){
        return userId;
    }
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isEnabled(){
        return true;
    }
    @Override
    public Map<String, Object>getClaims(){
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo(){
        return null;
    }
    @Override
    public OidcIdToken getIdToken(){
        return null;
    }
    public static MemberPrincipal create(Member member){
        return new MemberPrincipal(
                member.getUsername(),
                member.getPassword(),
                member.getSocialProvider(),
                Role.USER,
                Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getCode()))
        );
    }
    public static MemberPrincipal create(Member member, Map<String, Object> attributes){
        MemberPrincipal memberPrincipal = create(member);
        memberPrincipal.setAttributes(attributes);
        return memberPrincipal;
    }
}
