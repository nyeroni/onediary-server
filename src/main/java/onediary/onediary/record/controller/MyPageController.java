package onediary.onediary.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.member.entity.Member;
import onediary.onediary.member.dto.member.MyPageMemberDetailsResponseDto;
import onediary.onediary.member.dto.member.MyPageResponseDto;
import onediary.onediary.member.service.IMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "MyPage", description = "마이페이지 API")
@RequiredArgsConstructor
public class MyPageController {

    private final IMemberService memberService;

    @Operation(summary = "마이페이지 홈 조회", description = "마이페이지 홈을 조회합니다.")
    @GetMapping("/mypage/{memberId}")
    public ResponseEntity<MyPageResponseDto> getMyPage(@PathVariable Long memberId) {

        Member member = memberService.findMemberById(memberId);

        // 간단 내정보
        MyPageMemberDetailsResponseDto memberDetailsResponseDto = MyPageMemberDetailsResponseDto.from(member);
        // 일기 수
        int diaryCount = memberService.findCount(memberId);

        return ResponseEntity.ok(MyPageResponseDto.from(memberDetailsResponseDto, diaryCount));
    }

}
