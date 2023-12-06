package onediary.onediary.oauth.token;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class AuthResponseDto
{

    private String jwtToken;
    private String accessToken;
}
