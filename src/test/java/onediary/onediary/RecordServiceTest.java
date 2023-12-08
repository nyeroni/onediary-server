package onediary.onediary;

import onediary.onediary.component.DatabaseCleaner;
import onediary.onediary.member.entity.Member;
import onediary.onediary.member.entity.Role;
import onediary.onediary.member.entity.SocialProvider;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.record.Record;
import onediary.onediary.record.repository.RecordRepository;
import onediary.onediary.record.dto.record.RecordViewDto;
import onediary.onediary.record.dto.record.RecordWriteDto;
import onediary.onediary.record.service.IRecordService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class RecordServiceTest {

    @Autowired
    DatabaseCleaner databaseCleaner;

    @Autowired
    private IRecordService recordService;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    MemberRepository memberRepository;

    private Member member1;
    private Member member2;
    private RecordWriteDto record1Dto;
    private RecordWriteDto record2Dto;
    private RecordWriteDto record3Dto;
    private RecordWriteDto record4Dto;

    @BeforeEach
    void setUp() {
        LocalDateTime member1CreateTime = LocalDateTime.of(2023, 11, 20, 15, 30);
        LocalDateTime member2CreateTime = LocalDateTime.of(2023, 11, 21, 15, 35);
        // given
        member1 = Member.builder()
                .username("member1")
                .email("test@gmail.com")
                .socialProvider(SocialProvider.APPLE)
                .role(Role.USER)
                .build();


        member2 = Member.builder()
                .username("member2")
                .email("test@naver.com")
                .socialProvider(SocialProvider.NAVER)
                .role(Role.USER)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        LocalDate specificTime1 = LocalDate.of(2023, 1, 7);
        LocalDate specificTime2 = LocalDate.of(2023, 4, 5);
        LocalDate specificTime3 = LocalDate.of(2023, 7, 31);
        LocalDate specificTime4 = LocalDate.of(2023, 12, 25);

        record1Dto = RecordWriteDto.builder()
                .email(member1.getEmail())
                .description("안녕하세요.")
                .emotion("좋아")
                .recordDate(specificTime1)
                .build();
        record2Dto = RecordWriteDto.builder()
                .email(member1.getEmail())
                .description("반갑습니다.")
                .emotion("싫어")
                .recordDate(specificTime2)
                .build();
        record3Dto = RecordWriteDto.builder()
                .email(member1.getEmail())
                .description("테스트중입니다.")
                .emotion("메롱")
                .recordDate(specificTime3)
                .build();
        record4Dto = RecordWriteDto.builder()
                .email(member2.getEmail())
                .description("테스트용.")
                .emotion("하하")
                .recordDate(specificTime4)
                .build();

    }

    @AfterEach
    void tearDown() {
        databaseCleaner.truncateAllEntity();
    }

//    @Test
//    void findAllRecordsByMemberId() {
//        //when
//        recordService.createRecord(record1Dto, member1);
//        recordService.createRecord(record2Dto, member1);
//        recordService.createRecord(record3Dto, member1);
//        recordService.createRecord(record4Dto, member2);
//
//        System.out.println("userSeq ===== " + member1.getMemberId());
//        List<RecordViewDto> recordList = recordService.findAllRecordsByMemberId(member1.getMemberId());
//
//
//        //then
//
//        assertThat(recordList).isNotNull();
//        assertThat(recordList.size()).isEqualTo(3);
//        assertThat(recordList.get(0).getEmail()).isEqualTo(member1.getEmail());
//        assertThat(recordList.get(0).getDescription())
//                .isEqualTo(record1Dto.getDescription());
//    }
//    @Test
//    void findAllRecordsByEamil() {
//        //when
//        recordService.createRecord(record1Dto, member1);
//        recordService.createRecord(record2Dto, member1);
//        recordService.createRecord(record3Dto, member1);
//        recordService.createRecord(record4Dto, member2);
//
//
//        List<RecordViewDto> recordList = recordService.findAllRecordsByEmail(member1.getEmail());
//
//
//        //then
//
//        assertThat(recordList).isNotNull();
//        assertThat(recordList.size()).isEqualTo(3);
//        assertThat(recordList.get(0).getEmail()).isEqualTo(member1.getEmail());
//        assertThat(recordList.get(0).getDescription())
//                    .isEqualTo(record1Dto.getDescription());
//    }

    @Test
    void findRecordById() {
        //given
        RecordViewDto recordDto = recordService.createRecord(record1Dto, member1);
        Long recordId = recordDto.getId();
        //when
        RecordViewDto recordViewDto = recordService.findRecordById(recordId);
        //then
        assertThat(recordViewDto.getDescription()).isEqualTo(record1Dto.getDescription());
        assertThat(recordViewDto.getEmail()).isEqualTo(record1Dto.getEmail());
    }

    @Test
    void findRecordByCreatedDate(){
        //given
        RecordViewDto recordDto = recordService.createRecord(record1Dto, member1);
        Long recordId = recordDto.getId();
        //when

        Record createdRecord = recordRepository.findById(recordId).orElseThrow();
        Optional<RecordViewDto> recordByCreatedDate = recordService.findRecordByCreatedDate(createdRecord.getRecordDate());

        //then
        assertTrue(recordByCreatedDate.isPresent());
        RecordViewDto recordViewDto = recordByCreatedDate.orElseThrow();
        assertThat(recordViewDto.getDescription()).isEqualTo(record1Dto.getDescription());
        assertThat(recordViewDto.getEmail()).isEqualTo(record1Dto.getEmail());
        assertThat(recordViewDto.getId()).isEqualTo(recordId);

    }

    @Test
    void createRecord(){

        //when
        RecordViewDto recordDto = recordService.createRecord(record1Dto, member1);

        //then

        assertThat(recordDto.getDescription()).isEqualTo(record1Dto.getDescription());
        assertThat(recordDto.getEmail()).isEqualTo(record1Dto.getEmail());
    }
    @Test
    void updateRecordEmotion(){
        //given
        recordService.createRecord(record1Dto, member1);
        RecordViewDto recordDto = recordService.createRecord(record2Dto, member1);
        recordService.createRecord(record3Dto, member1);

        Long recordId = recordDto.getId();
        //when
        String newEmotion="기분 달라졌다";

        RecordViewDto updateRecordViewDto = recordService.updateRecordEmotion(recordId, newEmotion);
        //then

        assertThat(updateRecordViewDto.getEmotion()).isEqualTo(newEmotion);
        assertThat(updateRecordViewDto.getEmail()).isEqualTo(record2Dto.getEmail());
        assertThat(updateRecordViewDto.getDescription()).isEqualTo(record2Dto.getDescription());

    }
    @Test
    void updateRecordDescription(){
        //given
        recordService.createRecord(record1Dto, member1);
        RecordViewDto recordDto = recordService.createRecord(record2Dto, member1);
        recordService.createRecord(record3Dto, member1);

        Long recordId = recordDto.getId();
        //when
        String newDescription="바꿨다!";

        RecordViewDto updatedRecordViewDto = recordService.updateRecordDescription(recordId, newDescription);
        System.out.println("========"+updatedRecordViewDto.getDescription());

        //then
        assertThat(updatedRecordViewDto.getDescription()).isEqualTo(newDescription);
        assertThat(updatedRecordViewDto.getEmail()).isEqualTo(record2Dto.getEmail());
        assertThat(updatedRecordViewDto.getId()).isEqualTo(recordId);
        assertThat(updatedRecordViewDto.getEmotion()).isEqualTo(record2Dto.getEmotion());
        assertThat(updatedRecordViewDto.getRecordDate()).isEqualTo(record2Dto.getRecordDate());
    }

    @Transactional
    @Test
    void deleteRecord(){
        //given


        RecordViewDto recordDto = recordService.createRecord(record1Dto, member1);
        recordService.createRecord(record2Dto, member1);
        recordService.createRecord(record3Dto, member1);



        int beforeDeleteCount = member1.getRecordCount();
        System.out.println("before"+beforeDeleteCount);
        //when
        recordService.deleteRecord(recordDto.getId());

        int afterDeleteCount = member1.getRecordCount();
        System.out.println("after" + afterDeleteCount);
        //then
        assertThat(beforeDeleteCount).isEqualTo(3);
        assertThat(afterDeleteCount).isEqualTo(2);

    }

}