package onediary.onediary.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.domain.member.entity.Member;
import onediary.onediary.dto.member.MyPageMemberDetailsResponseDto;
import onediary.onediary.dto.member.MyPageResponseDto;
import onediary.onediary.service.IMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "MyPage", description = "마이페이지 API")
@RequiredArgsConstructor
public class MyPageController {

    private final IMemberService memberService;


    @GetMapping("/mypage/details")
    @Operation(summary = "내 정보 상세 조회", description = "현재 로그인한 사용자의 상세 정보 조회")
    public ResponseEntity<MyPageMemberDetailsResponseDto> getMyPageDetails() {
         Long currentUserId = getCurrentUserId();
        Member member = memberService.findMemberById(currentUserId);
        MyPageMemberDetailsResponseDto responseDto = MyPageMemberDetailsResponseDto.from(member);
        return ResponseEntity.ok(responseDto);
    }
    @Operation(summary = "마이페이지 홈 조회", description = "마이페이지 홈을 조회합니다.")
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDto> getMyPage(@RequestParam("memberId") Long memberId) {

        Member member = memberService.findMemberById(memberId);

        // 간단 내정보
        MyPageMemberDetailsResponseDto memberDetailsResponseDto = MyPageMemberDetailsResponseDto.from(member);
        // 일기 수
        int diaryCount = memberService.findCount(memberId);

        return ResponseEntity.ok(MyPageResponseDto.from(memberDetailsResponseDto, diaryCount));
    }
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Long.parseLong(userDetails.getUsername()); // 여기서 getUsername()이 memberId일 수 있습니다.
    }
}
