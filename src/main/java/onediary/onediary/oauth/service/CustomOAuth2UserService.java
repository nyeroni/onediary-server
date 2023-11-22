package onediary.onediary.oauth.service;

import lombok.RequiredArgsConstructor;
import onediary.onediary.api.domain.member.entity.Member;
import onediary.onediary.api.domain.member.entity.Role;
import onediary.onediary.api.domain.member.entity.SocialProvider;
import onediary.onediary.api.domain.member.repository.MemberRepository;
import onediary.onediary.oauth.entity.MemberPrincipal;
import onediary.onediary.oauth.exception.OAuthProviderMissMatchException;
import onediary.onediary.oauth.info.OAuth2UserInfo;
import onediary.onediary.oauth.info.OAuth2UserInfoFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User user = super.loadUser(userRequest);

        try{
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user){
        SocialProvider socialProvider = SocialProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialProvider, user.getAttributes());
        Member savedMember = memberRepository.findByUserId(userInfo.getId());
        if(savedMember!=null){
            if(socialProvider!=savedMember.getSocialProvider()){
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + socialProvider +
                                " account. Please use your " + savedMember.getSocialProvider() + " account to login."
                );
            }
            updateMember(savedMember, userInfo);
        }
        else{
            savedMember = createMember(userInfo, socialProvider);
        }
        return MemberPrincipal.create(savedMember, user.getAttributes());
    }
    private Member createMember (OAuth2UserInfo userInfo, SocialProvider socialProvider){
        LocalDateTime now = LocalDateTime.now();
        String email = userInfo.getEmail();
        // Ensure that the email field is not null or empty
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required but not provided by the OAuth2 provider.");
        }

        Member member = new Member(
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getEmail(),
                "Y",
                socialProvider,
                Role.USER,
                now,
                now
        );
        // Ensure that the email field is not null
        if (member.getEmail() == null) {
            throw new RuntimeException("Email is required but not provided by the OAuth2 provider.");
        }

        return memberRepository.saveAndFlush(member);
    }
    private Member updateMember(Member member, OAuth2UserInfo userInfo){
        if (userInfo.getName() != null && !member.getUsername().equals(userInfo.getName())) {
            member.setUsername(userInfo.getName());
        }

        // Ensure that the email field is not null
        if (userInfo.getEmail() != null && !member.getEmail().equals(userInfo.getEmail())) {
            member.setEmail(userInfo.getEmail());
        }

        return member;
    }
}
