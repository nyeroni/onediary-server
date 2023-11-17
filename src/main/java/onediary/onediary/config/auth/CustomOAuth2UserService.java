package onediary.onediary.config.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.config.auth.dto.OAuthAttributes;
import onediary.onediary.config.auth.dto.SessionUser;
import onediary.onediary.domain.member.Member;
import onediary.onediary.domain.member.MemberRepository;
import onediary.onediary.domain.member.Role;
import onediary.onediary.domain.member.SocialProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(oAuthAttributes);
        log.info("service attributes = {}", oAuthAttributes.getAttributes());
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.MEMBER.getKey())), oAuthAttributes.getAttributes(), oAuthAttributes.getNameAttributeKey());

    }
    private Member saveOrUpdate(OAuthAttributes attributes){
        Member member = memberRepository.findByEmailAndSocialProvider(attributes.getEmail(), SocialProvider.valueOf(attributes.getAttributes().get("socialProvider").toString())).orElse(attributes.toEntity());
        member.updateRoleKey(Role.MEMBER);
        return memberRepository.save(member);
    }
}
