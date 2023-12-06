package onediary.onediary.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import onediary.onediary.config.properties.AppProperties;
import onediary.onediary.domain.member.entity.Role;
import onediary.onediary.domain.member.entity.SocialProvider;
import onediary.onediary.oauth.info.OAuth2UserInfo;
import onediary.onediary.oauth.info.OAuth2UserInfoFactory;
import onediary.onediary.oauth.token.AuthToken;
import onediary.onediary.oauth.token.AuthTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Component //스프링 컨테스트에 이 클래스를 빈으로 등록
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;


    //인증이 성공했을 때 호출되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        //redirect 할 대상 url 결정
        String targetUrl = determineTargetUrl(request, response, authentication);
        if(response.isCommitted()){
            //이미 응답이 커밋되었는지 확인 후 디버그 메시지 출력
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request,response, targetUrl);
    }
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        Optional<String> redirectUri = Optional.ofNullable(request.getParameter("redirect_uri"));

        if(redirectUri.isPresent()&&!isAuthorizedRedirectUri(redirectUri.get())){
            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        SocialProvider socialProvider = SocialProvider.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = ((OidcUser) authentication.getPrincipal());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialProvider, user.getAttributes());
        Collection<? extends GrantedAuthority>authorities = ((OidcUser)authentication.getPrincipal()).getAuthorities();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String tokenExpiryString = dateFormat.format(new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));

        Role role = hasAuthority(authorities, Role.ADMIN.getCode())? Role.ADMIN:Role.USER;

        AuthToken accessToken = tokenProvider.createToken(
                userInfo.getId(),
                Role.USER,
                tokenExpiryString
        );



        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken.getToken())
                .build().toUriString();


    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response){
        super.clearAuthenticationAttributes(request);
    }

    private boolean hasAuthority(Collection<? extends  GrantedAuthority> authorities, String authority){
        if(authorities == null){
            return false;
        }
        for(GrantedAuthority grantedAuthority : authorities){
            if(authority.equals(grantedAuthority.getAuthority())){
                return true;
            }
        }
        return false;
    }
    private boolean isAuthorizedRedirectUri(String uri){
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOAuth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}