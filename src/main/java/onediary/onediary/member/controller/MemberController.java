package onediary.onediary.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.common.apiResponse.ApiResponse;
import onediary.onediary.member.dto.member.MemberResponseDto;
import onediary.onediary.record.dto.record.RecordViewDto;
import onediary.onediary.oauth.utils.JwtHeaderUtil;
import onediary.onediary.member.service.IMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final IMemberService memberService;

    @Operation(summary = "회원이 작성한 일기 리스트 조회", description = "현재 로그인한 회원의 일기 리스트 조회")
    @GetMapping("/records")
    public ResponseEntity<List<RecordViewDto>> getRecordList(HttpServletRequest request){
        try {
            String token = JwtHeaderUtil.getAccessToken(request);
            List<RecordViewDto> recordList = memberService.getRecordList(token);
            return new ResponseEntity<>(recordList, HttpStatus.OK);
        } catch (Exception e) {
            // 예외 처리 (필요에 따라 수정)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "로그인한 유저의 정보 조회", description = "로그인한 유저의 정보 조회")
    @GetMapping("/info")
    public ResponseEntity<MemberResponseDto> getUserInfo(HttpServletRequest request) {
        String token = JwtHeaderUtil.getAccessToken(request);
        return ApiResponse.success(memberService.getMemberResponseDto(token));
    }



}
