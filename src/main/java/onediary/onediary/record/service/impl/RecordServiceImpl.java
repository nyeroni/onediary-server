package onediary.onediary.record.service.impl;

import lombok.RequiredArgsConstructor;
import onediary.onediary.member.entity.Member;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.record.Record;
import onediary.onediary.record.repository.RecordQuerydslRepository;
import onediary.onediary.record.repository.RecordRepository;
import onediary.onediary.record.dto.record.RecordViewDto;
import onediary.onediary.record.dto.record.RecordWriteDto;
import onediary.onediary.oauth.service.AuthService;
import onediary.onediary.record.service.IRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements IRecordService {
    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final RecordQuerydslRepository recordQuerydslRepository;

    @Transactional(readOnly = true)
    @Override
    public RecordViewDto findRecordById(Long recordId) {

        Optional<Record> optionalRecord = Optional.ofNullable(recordRepository.findById(recordId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 베이커리 정보가 존재하지 않습니다."));

        return RecordViewDto.builder()
                .id(recordId)
                .description(optionalRecord.get().getDescription())
                .emotion(optionalRecord.get().getEmotion())
                .email(optionalRecord.get().getMember().getEmail())
                .recordDate(optionalRecord.get().getRecordDate())
                .currentScore(optionalRecord.get().getCurrentScore())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public RecordViewDto findRecordByCreatedDate(LocalDate createdDate) {
        System.out.println("createdDate="+createdDate);
        Optional<Record> recordOptional = recordRepository.findByCreatedDate(createdDate);

        return RecordViewDto.builder()
                .id(recordOptional.get().getId())
                .description(recordOptional.get().getDescription())
                .emotion(recordOptional.get().getEmotion())
                .email(recordOptional.get().getMember().getEmail())
                .recordDate(recordOptional.get().getRecordDate())
                .currentScore(recordOptional.get().getCurrentScore())
                .build();
    }

    @Transactional
    @Override
    public void createRecord(String token, RecordWriteDto recordDto){
        Long memberId = authService.getMemberId(token);
        Member member = memberRepository.findByMemberId(memberId);

        recordRepository.save(Record.builder()
                .description(recordDto.getDescription())
                .emotion(recordDto.getEmotion())
                .recordDate(recordDto.getRecordDate())
                .member(member)
                .build()
        );
    }
    @Transactional
    @Override
    public void updateRecordEmotion(Long recordId, String updatedEmotion){
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기가 없습니다. id=" + recordId));

        record.updateEmotion(updatedEmotion);

        recordRepository.save(record);


    }
    @Transactional
    @Override
    public void updateRecordDescription(Long recordId, String updatedDescription) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기가 없습니다. id=" + recordId));

        record.updateDescription(updatedDescription); // 기존 record를 업데이트

        recordRepository.save(record);

    }
    @Transactional
    @Override
    public void deleteRecord(String token, Long recordId){
        Long memberId = authService.getMemberId(token);
        Member member = memberRepository.findByMemberId(memberId);

        Record record = recordQuerydslRepository.findByRecordIdAndMemberId(recordId, memberId);
        member.getRecordList().remove(record);
        member.setRecordCount(member.getRecordCount());
        recordRepository.delete(record);


        memberRepository.save(member);
    }


}
