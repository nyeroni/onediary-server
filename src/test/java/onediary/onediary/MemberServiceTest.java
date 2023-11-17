package onediary.onediary;

import onediary.onediary.component.DatabaseCleaner;
import onediary.onediary.domain.member.Member;
import onediary.onediary.domain.member.MemberRepository;
import onediary.onediary.domain.member.SocialProvider;
import onediary.onediary.domain.record.Record;
import onediary.onediary.domain.record.RecordRepository;
import onediary.onediary.dto.member.MemberDto;
import onediary.onediary.service.IMemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    DatabaseCleaner databaseCleaner;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecordRepository recordRepository;

    private Member member1;
    private Member member2;


    @BeforeEach
    void setUp() {
        // given
        member1 = Member.builder()
                .username("member1")
                .email("test@gmail.com")
                .socialProvider(SocialProvider.APPLE)
                .build();

        member2 = Member.builder()
                .username("member2")
                .email("test2@naver.com")
                .socialProvider(SocialProvider.KAKAO)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.truncateAllEntity();
    }
    @Test
    void findMemberById() {
        // when
        MemberDto memberDto = memberService.findMemberById(member1.getId()).get();

        // then
        assertThat(memberDto.getUsername()).isEqualTo(member1.getUsername());
        assertThat(memberDto.getEmail()).isEqualTo(member1.getEmail());
    }

    @Test
    void findByEmail() {
        // when
        Long memberId = memberService.findByEmail(member1.getEmail());
        Member member = memberRepository.findById(memberId).get();


        // then
        assertThat(memberId).isEqualTo(member1.getId());
        assertThat(member.getEmail()).isEqualTo(member1.getEmail());
        assertThat(member.getUsername()).isEqualTo(member1.getUsername());
    }
    @Test
    void findCount() {
        //when
        String description1 = "안녕하세요.";
        String description2 = "반갑습니다.";
        String description3 = "테스트입니다.";


        recordRepository.save(Record.builder()
                .member(member1)
                .description(description1)
                .build());
        recordRepository.save(Record.builder()
                .member(member1)
                .description(description2)
                .build());
        recordRepository.save(Record.builder()
                .member(member1)
                .description(description3)
                .build());

        // when
        int count = memberService.findCount(member1.getId());

        // then
        assertThat(count).isEqualTo(3);
    }

}
