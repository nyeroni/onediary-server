package onediary.onediary.service.impl;

import lombok.RequiredArgsConstructor;
import onediary.onediary.common.exception.RecordNotFoundException;
import onediary.onediary.domain.member.entity.Member;
import onediary.onediary.domain.member.repository.MemberRepository;
import onediary.onediary.domain.record.Record;
import onediary.onediary.domain.record.RecordRepository;
import onediary.onediary.dto.record.RecordViewDto;
import onediary.onediary.dto.record.RecordWriteDto;
import onediary.onediary.service.IRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements IRecordService {
    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public List<RecordViewDto> findAllRecordsByMemberId(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow();
        System.out.println("KKKKKK"+ member.getEmail());
        List<RecordViewDto> recordViewDtos = new ArrayList<>();
        for(Record record : member.getRecordList()){
            recordViewDtos.add(RecordViewDto.toDto(record));
            System.out.println("+++++"+record.getDescription());
        }
        return recordViewDtos;
    }
    @Transactional(readOnly = true)
    @Override
    public RecordViewDto findRecordById(Long recordId) {
        Optional<Record> optionalRecord = recordRepository.findById(recordId);

        return optionalRecord.map(RecordViewDto::toDto).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<RecordViewDto> findRecordByCreatedDate(LocalDate createdDate) {
        System.out.println("createdDate="+createdDate);
        Optional<Record> recordOptional = recordRepository.findByCreatedDate(createdDate);

        return recordOptional.map(RecordViewDto::toDto);
    }

    @Transactional
    @Override
    public RecordViewDto createRecord(RecordWriteDto recordDto){
        String email = recordDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow();
        Record record = recordDto.toEntity(member);
        // 날짜 설정
       // record.setRecordDate(recordDto.getRecordDate());
        System.out.println("date===="+recordDto.getRecordDate());
        member.addRecord(record);
        recordRepository.save(record);

        return RecordViewDto.toDto(record);
    }
    @Transactional
    @Override
    public RecordViewDto updateRecordEmotion(Long recordId, String updatedEmotion){
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기가 없습니다. id=" + recordId));

        record.updateEmotion(updatedEmotion);

        recordRepository.save(record);

        return RecordViewDto.toDto(record);
    }
    @Transactional
    @Override
    public RecordViewDto updateRecordDescription(Long recordId, String updatedDescription) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기가 없습니다. id=" + recordId));

        record.updateDescription(updatedDescription); // 기존 record를 업데이트

        recordRepository.save(record);

        return RecordViewDto.toDto(record);
    }
    @Transactional
    @Override
    public void deleteRecord(Long recordId){
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RecordNotFoundException("Record not found with id: " + recordId));

        Member member = record.getMember();
        System.out.println("member_email" + member.getEmail());

        // recordList에서 제거
        member.getRecordList().remove(record);
        member.setRecordCount(member.getRecordCount());
        System.out.println("count===" + member.getRecordCount());

        // 레코드를 삭제
        recordRepository.delete(record);
        System.out.println("count===" + member.getRecordCount());



        memberRepository.save(member);

    }


}
