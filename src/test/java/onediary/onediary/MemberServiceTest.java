package onediary.onediary;

import onediary.onediary.component.DatabaseCleaner;
import onediary.onediary.member.entity.Member;
import onediary.onediary.member.entity.Role;
import onediary.onediary.member.entity.SocialProvider;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.member.service.IMemberService;
import onediary.onediary.record.Record;
import onediary.onediary.record.repository.RecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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
        String member1Email = "test1@daum.net";
        String member2Email = "test2@naver.com";
        member1 = Member.builder()
                .username("member1")
                .email(member1Email)
                .socialProvider(SocialProvider.KAKAO)
                .role(Role.USER)
                .build();

        member2 = Member.builder()
                .username("member2")
                .email(member2Email)
                .socialProvider(SocialProvider.NAVER)
                .role(Role.USER)
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
        Member findMember = memberService.findMemberById(member1.getId());

        // then
        assertThat(findMember.getUsername()).isEqualTo(member1.getUsername());
        assertThat(findMember.getEmail()).isEqualTo(member1.getEmail());
    }

    @Test
    void findCount() {
        //when
        String description1 = "안녕하세요.";
        String description2 = "반갑습니다.";
        String description3 = "테스트입니다.";
        String emotion1 = "happy";
        String emotion2 = "sad";
        String emotion3 = "하하";


        Record r1 = recordRepository.save(Record.builder()
                .member(member1)
                .description(description1)
                .emotion(emotion1)
                .build());
        Record r2 = recordRepository.save(Record.builder()
                .member(member1)
                .description(description2)
                        .emotion(emotion2)
                .build());
        Record r3 = recordRepository.save(Record.builder()
                .member(member1)
                .description(description3)
                        .emotion(emotion3)
                .build());
        member1.addRecord(r1);
        member1.addRecord(r2);
        member1.addRecord(r3);

        // when
        int count = memberService.findCount(member1.getId());

        // then
        assertThat(count).isEqualTo(3);
    }




}
