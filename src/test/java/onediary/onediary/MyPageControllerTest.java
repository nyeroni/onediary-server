package onediary.onediary;

import onediary.onediary.mainpage.dto.MainPageMemberDetailsResponseDto;
import onediary.onediary.mainpage.service.impl.MainPageServiceImpl;
import onediary.onediary.member.dto.member.MemberResponseDto;
import onediary.onediary.member.repository.MemberQuerydslRepository;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.oauth.service.AuthService;
import onediary.onediary.record.repository.RecordQuerydslRepository;
import onediary.onediary.record.repository.RecordRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(TestSecurityConfig.class)
@SpringBootTest
public class MyPageControllerTest {

    @InjectMocks
    private MainPageServiceImpl mainPageService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberQuerydslRepository memberQuerydslRepository;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private RecordQuerydslRepository recordQuerydslRepository;

    @Mock
    private AuthService authService;
    @Test
    void testGetMainPageDetail() {
        // given
        String token = "mockToken";
        Long memberId = 1L;
        String username = "mockUser";
        Integer diaryCount = 5;

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setId(memberId);
        memberResponseDto.setUserName(username);
        memberResponseDto.setRecordCount(diaryCount);

        given(authService.getMemberId(anyString())).willReturn(memberId);
        given(memberQuerydslRepository.findByMemberId(memberId)).willReturn(memberResponseDto);

        // when
        MainPageMemberDetailsResponseDto responseDto = mainPageService.getMainPageDetail(token);

        // then
        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getMemberId()).isEqualTo(memberId);
        Assertions.assertThat(responseDto.getUsername()).isEqualTo(username);
        Assertions.assertThat(responseDto.getDiaryCount()).isEqualTo(diaryCount);
    }
    @Test
    void testGetMainPageDetailMemberNotFound() {
        // given
        String token = "mockToken";
        Long memberId = 1L;

        given(authService.getMemberId(anyString())).willReturn(memberId);
        given(memberQuerydslRepository.findByMemberId(memberId)).willReturn(null);

        // when & then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> mainPageService.getMainPageDetail(token));
        Assertions.assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(exception.getReason()).isEqualTo("해당 유저가 없습니다.");
    }

}
