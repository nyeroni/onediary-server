package onediary.onediary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthController {

    @ResponseBody
    @GetMapping("/api/oauth/kakao")
    public void kakaoCallback(@RequestParam String code){
        log.info("code : "+ code);
    }
}
