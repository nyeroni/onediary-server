package onediary.onediary;

import onediary.onediary.member.domain.Member;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.oauth.service.AuthService;
import onediary.onediary.record.domain.Record;
import onediary.onediary.record.dto.record.RecordViewDto;
import onediary.onediary.record.dto.record.RecordWriteDto;
import onediary.onediary.record.repository.RecordQuerydslRepository;
import onediary.onediary.record.repository.RecordRepository;
import onediary.onediary.record.service.impl.RecordServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
@Import(TestSecurityConfig.class) // JWT 인증과정을 무시하기 위해 사용
public class RecordServiceTest {


    @InjectMocks
    private RecordServiceImpl recordService;

    @Mock
   private RecordRepository recordRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthService authService;

    @Mock
    private RecordQuerydslRepository recordQuerydslRepository;

    @Mock
    Member member1;  // Mocked member


    @Test
    void findRecordById() {
        //given
        Long recordId = 1L;
        Record record = new Record();
        record.setId(recordId);
        record.setDescription("Test description");
        record.setEmotion("Happy");
        record.setRecordDate(LocalDate.now());
        record.setMember(member1);
        given(recordRepository.findById(recordId)).willReturn(Optional.of(record));

        // when
        RecordViewDto recordViewDto = recordService.findRecordById(recordId);

        // then
        Assertions.assertThat(recordViewDto.getId()).isEqualTo(recordId);
        Assertions.assertThat(recordViewDto.getDescription()).isEqualTo("Test description");
        Assertions.assertThat(recordViewDto.getEmotion()).isEqualTo("Happy");
        Assertions.assertThat(recordViewDto.getEmail()).isEqualTo(member1.getEmail());

    }

    @Test
    void findRecordByCreatedDate(){
        // given
        LocalDate createdDate = LocalDate.now();

        Long recordId = 1L;
        Record record = new Record();
        record.setId(recordId);
        record.setDescription("Test description");
        record.setEmotion("Happy");
        record.setRecordDate(createdDate);
        record.setMember(member1);
        given(recordRepository.findByCreatedDate(createdDate)).willReturn(Optional.of(record));
        // when
        RecordViewDto recordViewDto = recordService.findRecordByCreatedDate(createdDate);

        // then
        Assertions.assertThat(recordViewDto).isNotNull();
        Assertions.assertThat(recordViewDto.getId()).isEqualTo(recordId);
        Assertions.assertThat(recordViewDto.getDescription()).isEqualTo("Test description");
        Assertions.assertThat(recordViewDto.getEmotion()).isEqualTo("Happy");
        Assertions.assertThat(recordViewDto.getRecordDate()).isEqualTo(createdDate);
        Assertions.assertThat(recordViewDto.getEmail()).isEqualTo(member1.getEmail());

    }

    @Test
    void createRecord(){
        //given
        String token = "sampleToken";
        RecordWriteDto recordDto = new RecordWriteDto();
        recordDto.setDescription("Test description");
        recordDto.setEmotion("Happy");
        recordDto.setRecordDate(LocalDate.now());

        Long memberId = 1L;
        Member member = new Member();
        member.setId(memberId);

        given(authService.getMemberId(token)).willReturn(memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        recordService.createRecord(token, recordDto);

        // then

        verify(recordRepository, times(1)).save(argThat(record ->
                record.getDescription().equals("Test description") &&
                        record.getEmotion().equals("Happy") &&
                        record.getRecordDate().equals(LocalDate.now()) &&
                        record.getMember().equals(member)
        ));
    }
    @Test
    void updateRecordEmotion(){
        // given
        Long recordId = 1L;
        String updatedEmotion = "Excited";

        Record existingRecord = new Record();
        existingRecord.setId(recordId);
        existingRecord.setEmotion("Happy");

        given(recordRepository.findById(recordId)).willReturn(Optional.of(existingRecord));

        // when
        recordService.updateRecordEmotion(recordId, updatedEmotion);

        // then
        verify(recordRepository, times(1)).save(argThat(record ->
                        record.getId().equals(recordId) &&
                                record.getEmotion().equals(updatedEmotion)
                // Add more assertions based on your requirements
        ));
    }
    @Test
    void updateRecordDescription(){

        // given
        Long recordId = 1L;
        String updatedDescription = "Updated description";

        Record existingRecord = new Record();

        existingRecord.setId(recordId);
        existingRecord.setDescription("Original description");

        given(recordRepository.findById(recordId)).willReturn(Optional.of(existingRecord));

        // when
        recordService.updateRecordDescription(recordId, updatedDescription);

        // then
        verify(recordRepository, times(1)).save(argThat(record ->
                        record.getId().equals(recordId) &&
                                record.getDescription().equals(updatedDescription)
                // Add more assertions based on your requirements
        ));
    }

    @Transactional
    @Test
    void deleteRecord(){
        // given
        String token = "sampleToken";
        Long recordId = 1L;

        Long memberId = 1L;
        Member member = new Member();
        member.setId(memberId);

        Record record = new Record();
        record.setId(recordId);
        record.setMember(member);

        given(authService.getMemberId(token)).willReturn(memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(recordQuerydslRepository.findByRecordIdAndMemberId(recordId, memberId)).willReturn(record);

        // when
        recordService.deleteRecord(token, recordId);

        // then
        verify(recordRepository, times(1)).delete(record);
        verify(memberRepository, times(1)).save(member);

    }

}