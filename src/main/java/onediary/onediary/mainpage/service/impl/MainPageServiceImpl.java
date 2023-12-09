package onediary.onediary.mainpage.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.mainpage.dto.MyPageMemberDetailsResponseDto;
import onediary.onediary.mainpage.service.IMainPageService;
import onediary.onediary.member.dto.member.MemberResponseDto;
import onediary.onediary.member.repository.MemberQuerydslRepository;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.oauth.service.AuthService;
import onediary.onediary.record.repository.RecordQuerydslRepository;
import onediary.onediary.record.repository.RecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainPageServiceImpl  implements IMainPageService {

    private final MemberRepository memberRepository;
    private final MemberQuerydslRepository memberQuerydslRepository;
    private final RecordRepository recordRepository;
    private final RecordQuerydslRepository recordQuerydslRepository;
    private final AuthService authService;


    @Override
    @Transactional(readOnly = true)
    public MyPageMemberDetailsResponseDto getMainPageDetail(String token){

        Long memberId = authService.getMemberId(token);

        MemberResponseDto member = memberQuerydslRepository.findByMemberId(memberId);
        if(member==null){
            new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 없습니다.");
        }
        return MyPageMemberDetailsResponseDto.builder()
                .username(member.getUserName())
                .memberId(memberId)
                .diaryCount(member.getRecordCount())
                .build();
    }




}
