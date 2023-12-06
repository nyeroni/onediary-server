package onediary.onediary.oauth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.domain.member.repository.MemberQuerydslRepository;
import onediary.onediary.dto.member.MemberResponseDto;
import onediary.onediary.oauth.token.AuthToken;
import onediary.onediary.oauth.token.AuthTokenProvider;
import onediary.onediary.service.IMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor

public class AuthService {

    private final AuthTokenProvider authTokenProvider;
    private final MemberQuerydslRepository memberQuerydslRepository;
    private final IMemberService memberService;
    private final KakaoOauthService kakaoOauthService;

    //카카오 로그인
    public AuthToken loginKakao(String accessToken, HttpServletResponse response) {
        // 카카오로부터 사용자 정보를 받아옴
        MemberResponseDto memberResponseDto = kakaoOauthService.getUserProfileByToken(accessToken);

        // 만약 해당 socialId로 등록된 회원이 없다면 새로 등록
        if (memberService.findMemberBySocialId(memberResponseDto.getSocialId()) == null) {
            memberService.save(memberResponseDto);
        }

        // 액세스 토큰 발급 및 반환
        return authTokenProvider.createMemberJwtToken(memberResponseDto.getSocialId());

    }
    //액세스토큰, 리프레시토큰 생성
    public AuthToken getTokens(String socialId, HttpServletResponse response){
        final AuthToken accessToken = authTokenProvider.createMemberJwtToken(socialId);
        final AuthToken refreshToken = authTokenProvider.createRefreshToken(socialId);

         memberService.updateRefreshToken(socialId, refreshToken.getToken());

        return accessToken;
    }
    // 리프레시 토큰으로 액세스토큰 새로 갱신
    public AuthToken refreshAccessToken(String refreshToken) {
        // 리프레시 토큰을 이용하여 회원 정보 조회
        MemberResponseDto memberResponseDto = memberService.findByRefreshToken(refreshToken);

        // 리프레시 토큰이 유효하지 않은 경우 예외 처리
        if (memberResponseDto == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Refresh Token");
        }

        // 새로운 액세스 토큰 발급 및 반환
        return authTokenProvider.refreshToken(refreshToken); }
}