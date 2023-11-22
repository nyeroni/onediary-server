package onediary.onediary.api.controller;

import lombok.RequiredArgsConstructor;
import onediary.onediary.api.domain.member.entity.Member;
import onediary.onediary.api.service.IMemberService;
import onediary.onediary.common.ApiResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vi/users")
@RequiredArgsConstructor
public class UserController {

    private final IMemberService memberService;

    @GetMapping
    public ApiResponse getMember(){
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberService.getMember((principal.getUsername()));
        return ApiResponse.success("member", member);
    }
}
