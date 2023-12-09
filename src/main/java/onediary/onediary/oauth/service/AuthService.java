package onediary.onediary.oauth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.member.domain.Member;
import onediary.onediary.member.repository.MemberQuerydslRepository;
import onediary.onediary.oauth.token.AuthResponseDto;
import onediary.onediary.oauth.token.AuthToken;
import onediary.onediary.oauth.token.AuthTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor

public class AuthService {

    private final AuthTokenProvider authTokenProvider;
    private final MemberQuerydslRepository memberQuerydslRepository;
    public AuthResponseDto updateToken(AuthToken authToken) {
        Claims claims = authToken.getTokenClaims();
        if (claims == null) {
            return null;
        }

        String socialId = claims.getSubject();

        AuthToken newAppToken = authTokenProvider.createMemberJwtToken(socialId);

        return AuthResponseDto.builder()
                .jwtToken(newAppToken.getToken())
                .build();
    }
    public Long getMemberId(String token) {
        AuthToken authToken = authTokenProvider.convertAuthToken(token);

        Claims claims = authToken.getTokenClaims();
        if (claims == null) {
            return null;
        }

        try {
            Member member =  memberQuerydslRepository.findBySocialId(claims.getSubject());
            return member.getId();

        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.");
        }
    }
}