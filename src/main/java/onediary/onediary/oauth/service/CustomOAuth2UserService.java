//package onediary.onediary.oauth.service;
//
//import lombok.RequiredArgsConstructor;
//import onediary.onediary.common.exception.OAuthProviderMissMatchException;
//import onediary.onediary.member.entity.Member;
//import onediary.onediary.member.entity.Role;
//import onediary.onediary.member.entity.SocialProvider;
//import onediary.onediary.member.repository.MemberQuerydslRepository;
//import onediary.onediary.member.repository.MemberRepository;
//import onediary.onediary.oauth.info.OAuth2UserInfo;
//import onediary.onediary.oauth.info.OAuth2UserInfoFactory;
//import onediary.onediary.oauth.token.AuthTokenProvider;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//@RequiredArgsConstructor
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    private final AuthTokenProvider authTokenProvider;  // 추가
//    private final MemberRepository memberRepository;
//    private final MemberQuerydslRepository memberQuerydslRepository;
//
//
//    // OAuth2 제공자에서 사용자 정보를 로드하는 메서드
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
//        // 로드된 사용자 정보를 가지고 추가 처리를 수행하는 메서드 호출
//        OAuth2User user = super.loadUser(userRequest);
//
//        try{
//            return this.process(userRequest, user);
//        } catch (AuthenticationException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
//        }
//    }
//    // 사용자 정보를 추가로 처리하는 메서드
//    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user){
//        // OAuth2 클라이언트 등록 정보로부터 제공자를 확인
//        SocialProvider socialProvider = SocialProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
//        // 사용자 정보를 추상화한 OAuth2UserInfo 인스턴스 생성
//        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialProvider, user.getAttributes());
//
//        Member savedMember = memberQuerydslRepository.findBySocialId(userInfo.getId());
//        if(savedMember!=null){
//            // 이미 회원 정보가 존재하면 제공자가 일치하는지 확인
//            if(socialProvider!=savedMember.getSocialProvider()){
//                // 일치하지 않으면 OAuthProviderMissMatchException 발생
//                throw new OAuthProviderMissMatchException(
//                        "현재 " + socialProvider +
//                                " 계정으로 가입되어 있습니다. " + savedMember.getSocialProvider() + " 계정으로 로그인하세요."
//                );
//            }
//            // 일치하면 회원 정보 업데이트
//            updateMember(savedMember, userInfo);
//        }
//        else{
//            // 회원 정보가 없으면 신규 회원 생성
//            savedMember = createMember(userInfo, socialProvider);
//        }
//        // OAuth2 사용자 정보 반환
//        return user;
//    }
//    // Apple 제공자의 사용자 정보를 처리하는 메서드 (미구현)
//    private Member processAppleUser(OAuth2UserInfo userInfo, SocialProvider socialProvider) {
//        Member savedMember = memberQuerydslRepository.findBySocialId(userInfo.getId());
//
//        if (savedMember != null) {
//            if (socialProvider != savedMember.getSocialProvider()) {
//                throw new OAuthProviderMissMatchException(
//                        "Looks like you're signed up with " + socialProvider +
//                                " account. Please use your " + savedMember.getSocialProvider() + " account to login."
//                );
//            }
//            updateMember(savedMember, userInfo);
//        } else {
//            savedMember = createMember(userInfo, socialProvider);
//        }
//
//        return savedMember;
//    }
//    // 회원 정보 생성 메서드
//    private Member createMember (OAuth2UserInfo userInfo, SocialProvider socialProvider){
//        LocalDateTime now = LocalDateTime.now();
//        String email = userInfo.getEmail();
//        // 이메일 필드가 null 또는 비어 있지 않도록 확인
//        if (email == null || email.isEmpty()) {
//            throw new RuntimeException("Email is required but not provided by the OAuth2 provider.");
//        }
//        // 회원 정보 빌더를 사용하여 인스턴스 생성
//        Member member = Member.builder()
//                .username(userInfo.getName())
//                .email(userInfo.getEmail())
//                .socialProvider(socialProvider)
//                .socialId(userInfo.getId())
//                .role(Role.USER)
//                .build();
//        // 이메일 필드가 null이 아닌지 확인
//        if (member.getEmail() == null) {
//            throw new RuntimeException("Email is required but not provided by the OAuth2 provider.");
//        }
//
//        // 회원 정보 저장
//        return memberRepository.saveAndFlush(member);
//    }
//    // 회원 정보 업데이트 메서드
//    private Member updateMember(Member member, OAuth2UserInfo userInfo){
//        // 이름이 존재하고 현재 저장된 이름과 다르면 업데이트
//        if (userInfo.getName() != null && !member.getUsername().equals(userInfo.getName())) {
//            member.setUsername(userInfo.getName());
//        }
//
//        // 이메일이 존재하고 현재 저장된 이메일과 다르면 업데이트
//        if (userInfo.getEmail() != null && !member.getEmail().equals(userInfo.getEmail())) {
//            member.setEmail(userInfo.getEmail());
//        }
//
//        return memberRepository.saveAndFlush(member);
//    }
//}