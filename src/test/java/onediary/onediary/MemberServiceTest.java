package onediary.onediary;

import onediary.onediary.member.domain.Member;
import onediary.onediary.member.dto.member.MemberResponseDto;
import onediary.onediary.member.repository.MemberQuerydslRepository;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.member.service.impl.MemberServiceImpl;
import onediary.onediary.oauth.service.AuthService;
import onediary.onediary.record.domain.Record;
import onediary.onediary.record.dto.record.RecordViewDto;
import onediary.onediary.record.repository.RecordRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestSecurityConfig.class) // JWT 인증과정을 무시하기 위해 사용
public class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private MemberQuerydslRepository memberQuerydslRepository;

    @Mock
    private AuthService authService;



    @Test
    void 회원의_총_일기_개수_반환() {
        //given

        MemberResponseDto memberDto = new MemberResponseDto(1L, "member1", "test1@daum.net", 3);
        given(memberQuerydslRepository.findByMemberId(anyLong())).willReturn(memberDto);

        // when
        int count = memberService.findCount(1L);

        // then
        assertThat(count).isEqualTo(3);

    }

    @Test
    public void 회원의_일기_목록_반환(){
        //given
        Long memberId = 1L;
        String accessToken = "sampleAccessToken";
        given(authService.getMemberId(accessToken)).willReturn(memberId);

        Member member = new Member();
        member.setId(memberId);
        member.setEmail("test");

        Record record1 = new Record();
        record1.setDescription("안녕하세요.");
        record1.setEmotion("happy");


        Record record2 = new Record();
        record2.setDescription("반갑습니다.");
        record2.setEmotion("sad");

        member.addRecord(record1);
        member.addRecord(record2);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // when
        List<RecordViewDto> recordList = memberService.getRecordList(accessToken);

        // then
        assertThat(recordList).hasSize(2);
        assertThat(recordList.get(0).getDescription()).isEqualTo("안녕하세요.");
        assertThat(recordList.get(1).getDescription()).isEqualTo("반갑습니다.");

    }

    @Test
    public void 회원_정보_반환() {
        // given
        Long memberId = 1L;
        String accessToken = "sampleAccessToken";
        given(authService.getMemberId(accessToken)).willReturn(memberId);

        MemberResponseDto memberDto = new MemberResponseDto(memberId, "member1", "test1@daum.net", 3);
        given(memberQuerydslRepository.findByMemberId(memberId)).willReturn(memberDto);

        // when
        MemberResponseDto responseDto = memberService.getMemberResponseDto(accessToken);

        // then
        assertThat(responseDto.getUserName()).isEqualTo("member1");
        assertThat(responseDto.getEmail()).isEqualTo("test1@daum.net");
        assertThat(responseDto.getRecordCount()).isEqualTo(3);
    }

}
