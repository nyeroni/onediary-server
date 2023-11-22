//package onediary.onediary;
//
//import onediary.onediary.api.component.DatabaseCleaner;
//import onediary.onediary.api.domain.member.entity.Member;
//import onediary.onediary.api.domain.member.repository.MemberRepository;
//import onediary.onediary.api.domain.member.entity.SocialProvider;
//import onediary.onediary.api.domain.record.Record;
//import onediary.onediary.api.domain.record.RecordRepository;
//import onediary.onediary.api.dto.record.RecordViewDto;
//import onediary.onediary.api.dto.record.RecordWriteDto;
//import onediary.onediary.api.service.IRecordService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.fail;
//import static org.hibernate.validator.internal.util.Contracts.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//@SpringBootTest
//public class RecordServiceTest {
//
//    @Autowired
//    DatabaseCleaner databaseCleaner;
//
//    @Autowired
//    private IRecordService recordService;
//
//    @Autowired
//    RecordRepository recordRepository;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    private Member member1;
//    private Member member2;
//    private RecordWriteDto record1Dto;
//    private RecordWriteDto record2Dto;
//    private RecordWriteDto record3Dto;
//    private RecordWriteDto record4Dto;
//
//    @BeforeEach
//    void setUp() {
//        // given
//        member1 = Member.builder()
//                .username("member1")
//                .email("test@gmail.com")
//                .socialProvider(SocialProvider.APPLE)
//                .build();
//
//
//        member2 = Member.builder()
//                .username("member2")
//                .email("test@naver.com")
//                .socialProvider(SocialProvider.NAVER)
//                .build();
//        memberRepository.save(member1);
//        memberRepository.save(member2);
//        record1Dto = RecordWriteDto.builder()
//                .email(member1.getEmail())
//                .description("안녕하세요.")
//                .build();
//        record2Dto = RecordWriteDto.builder()
//                .email(member1.getEmail())
//                .description("반갑습니다.")
//                .build();
//        record3Dto = RecordWriteDto.builder()
//                .email(member1.getEmail())
//                .description("테스트중입니다.")
//                .build();
//        record4Dto = RecordWriteDto.builder()
//                .email(member2.getEmail())
//                .description("테스트용.")
//                .build();
//
//    }
//
//    @AfterEach
//    void tearDown() {
//        databaseCleaner.truncateAllEntity();
//    }
//
//    @Test
//    void findAllRecords() {
//        //when
//        recordService.createRecord(record1Dto);
//        recordService.createRecord(record2Dto);
//        recordService.createRecord(record3Dto);
//        recordService.createRecord(record4Dto);
//
//        List<RecordViewDto> recordList = recordService.findAllRecords(member1.getEmail());
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
//
//    @Test
//    void findRecordById() {
//        //given
//        Long recordId = recordService.createRecord(record1Dto);
//        //when
//        RecordViewDto recordViewDto = recordService.findRecordById(recordId);
//        //then
//        assertThat(recordViewDto.getDescription()).isEqualTo(record1Dto.getDescription());
//        assertThat(recordViewDto.getEmail()).isEqualTo(record1Dto.getEmail());
//    }
//
//    @Test
//    void findRecordByCreatedDate(){
//        //given
//        Long recordId = recordService.createRecord(record1Dto);
//
//        //when
//        Record createdRecord = recordRepository.findById(recordId).orElseThrow();
//
//        Optional<Long> recordByCreatedDate = recordService.findRecordByCreatedDate(createdRecord.getCreatedDate());
//        //then
//        assertTrue(recordByCreatedDate.isPresent(), "레코드를 찾을 수 없습니다.");
//
//        recordByCreatedDate.ifPresentOrElse(
//                id -> {
//                    RecordViewDto recordViewDto = recordService.findRecordById(id);
//                    assertThat(recordViewDto.getDescription()).isEqualTo(record1Dto.getDescription());
//                    assertThat(recordViewDto.getEmail()).isEqualTo(record1Dto.getEmail());
//                    assertThat(recordViewDto.getId()).isEqualTo(recordId);
//                },
//                () -> fail("레코드를 찾을 수 없습니다.")
//        );
//
//    }
//
//    @Test
//    void createRecord(){
//
//        //when
//        Long recordId = recordService.createRecord(record1Dto);
//
//        List<RecordViewDto> recordViewDtos = recordService.getRecords(member1.getUserSeq());
//        //then
//        RecordViewDto recordViewDto = recordService.findRecordById(recordId);
//        Member member = memberRepository.findByEmail(recordViewDto.getEmail()).get();
//
//        assertThat(recordViewDtos.size()).isEqualTo(1);
//        assertThat(recordViewDtos.get(0).getDescription()).isEqualTo(record1Dto.getDescription());
//        assertThat(recordViewDtos.get(0).getEmail()).isEqualTo(record1Dto.getEmail());
//    }
//
//    @Test
//    void updateRecordDescription(){
//        //given
//        recordService.createRecord(record1Dto);
//        Long recordId = recordService.createRecord(record2Dto);
//        recordService.createRecord(record3Dto);
//        //when
//        String newDescription="바꿨다!";
//
//        recordService.updateRecordDescription(recordId,newDescription);
//        //then
//        RecordViewDto updateRecordViewDto = recordService.findRecordById(recordId);
//        Optional<Member> optionalMember = memberRepository.findByEmail(updateRecordViewDto.getEmail());
//        Member member = optionalMember.get();
//        List<RecordViewDto> recordViewDtos = recordService.getRecords(member.getId());
//
//        assertThat(updateRecordViewDto.getDescription()).isEqualTo(newDescription);
//        assertThat(updateRecordViewDto.getEmail()).isEqualTo(record2Dto.getEmail());
//        assertThat(recordViewDtos.size()).isEqualTo(3);
//    }
//
//    @Test
//    void deleteRecord(){
//        //given
//        Long recordId1 = recordService.createRecord(record1Dto);
//        recordService.createRecord(record2Dto);
//        recordService.createRecord(record3Dto);
//
//        Member member = memberRepository.findByEmail(record1Dto.getEmail()).get();
//
//        List<RecordViewDto> recordViewDtos = recordService.getRecords(member.getId());
//        int recordSize = recordViewDtos.size();
//        System.out.println("recordSize:"+recordSize);
//        //when
//        recordService.deleteRecord(recordId1);
//        List<RecordViewDto> deleteRecordViewDtos = recordService.getRecords(member.getId());
//
//        //then
//        assertThat(deleteRecordViewDtos.size()).isEqualTo(2);
//
//    }
//}