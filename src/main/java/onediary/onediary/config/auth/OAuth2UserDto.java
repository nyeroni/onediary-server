package onediary.onediary.config.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onediary.onediary.domain.member.SocialProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OAuth2UserDto {
    private String email;
    private String name;
    private SocialProvider socialProvider;

    public static OAuth2UserDto toDto(OAuth2User oAuth2User){
        var attributes = oAuth2User.getAttributes();
        return OAuth2UserDto.builder()
                .email((String )attributes.get("email"))
                .name((String)attributes.get("name"))
                .socialProvider(SocialProvider.valueOf(attributes.get("socialProvider").toString()))
                .build();

    }
}
