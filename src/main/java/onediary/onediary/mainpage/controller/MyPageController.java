package onediary.onediary.mainpage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.common.apiResponse.ApiResponse;
import onediary.onediary.mainpage.dto.MyPageMemberDetailsResponseDto;
import onediary.onediary.mainpage.service.IMainPageService;
import onediary.onediary.member.service.IMemberService;
import onediary.onediary.oauth.utils.JwtHeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "MyPage", description = "마이페이지 API")
@RequiredArgsConstructor
public class MyPageController {

    private final IMemberService memberService;
    private final IMainPageService mainPageService;

    @Operation(summary = "마이페이지 홈 조회", description = "마이페이지 홈을 조회합니다.")
    @GetMapping("/mypage/{memberId}")
    public ResponseEntity<MyPageMemberDetailsResponseDto> getMyPage(HttpServletRequest request) {

        String token = JwtHeaderUtil.getAccessToken(request);


        return ApiResponse.success(mainPageService.getMainPageDetail(token));
    }

}
