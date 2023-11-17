package onediary.onediary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.config.jwt.TokenBlackList;
import onediary.onediary.config.jwt.TokenBlackListRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final TokenBlackListRepository tokenBlackListRepository;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization")String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");

        TokenBlackList blackList = new TokenBlackList(token);
        tokenBlackListRepository.save(blackList);
        return ResponseEntity.ok("Logged out successfully");
    }
}
