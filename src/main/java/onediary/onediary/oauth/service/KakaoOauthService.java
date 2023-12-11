package onediary.onediary.oauth.service;

import lombok.RequiredArgsConstructor;
import onediary.onediary.member.domain.Member;
import onediary.onediary.member.repository.MemberQuerydslRepository;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.oauth.client.ClientKakao;
import onediary.onediary.oauth.token.AuthRequestDto;
import onediary.onediary.oauth.token.AuthResponseDto;
import onediary.onediary.oauth.token.AuthToken;
import onediary.onediary.oauth.token.AuthTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class KakaoOauthService {
    private final ClientKakao clientKakao;
    private final MemberQuerydslRepository memberQuerydslRepository;
    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        Member kakaoMember = clientKakao.getUserData(authRequestDto.getAccessToken());
        String socialId = kakaoMember.getSocialId();
        Member member = memberQuerydslRepository.findBySocialId(socialId);

        AuthToken appToken = authTokenProvider.createMemberJwtToken(socialId);

        if (member == null) {
            memberRepository.save(kakaoMember);
            return AuthResponseDto.builder()
                    .jwtToken(appToken.getToken())
                    .isNewMember(Boolean.TRUE)
                    .build();
        }

        return AuthResponseDto.builder()
                .jwtToken(appToken.getToken())
                .isNewMember(Boolean.FALSE)
                .build();
    }
}
