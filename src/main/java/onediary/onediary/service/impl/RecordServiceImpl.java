package onediary.onediary.service.impl;

import lombok.RequiredArgsConstructor;
import onediary.onediary.domain.member.Member;
import onediary.onediary.domain.member.MemberRepository;
import onediary.onediary.domain.record.Record;
import onediary.onediary.domain.record.RecordRepository;
import onediary.onediary.dto.record.record.RecordDto;
import onediary.onediary.service.IRecordService;
import onediary.onediary.service.exception.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements IRecordService {
    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    @Override
    public List<RecordDto> findAllRecords(String email){
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if(optionalMember.isPresent()){
            List<Record> allRecords = recordRepository.findAllByEmail(email);

            return allRecords.stream()
                    .map(RecordDto::toDto)
                    .collect(Collectors.toList());
        }
        else{
            return Collections.emptyList();
        }

    }
    @Override
    public Optional<RecordDto> findRecordById(Long recordId){
        Optional<Record> optionalRecord = recordRepository.findById(recordId);
        return optionalRecord.map(RecordDto::toDto);
    }
    @Override
    public Optional<RecordDto> findRecordByCreatedDate(LocalDateTime createdDate){
        Optional<Record> optionalRecord = recordRepository.findByDate(createdDate);
        return optionalRecord.map(RecordDto::toDto);
    }
    @Override
    public RecordDto createRecord(RecordDto recordDto){
        Record record = recordDto.toEntity();

        Record savedRecord = recordRepository.save(record);
        return RecordDto.toDto(savedRecord);
    }
    @Override
    public RecordDto updateRecordDescription(Long recordId, String updatedDescription){
        Optional<Record> optionalRecord = recordRepository.findById(recordId);

        if(optionalRecord.isPresent()){
            Record record = optionalRecord.get();
            Record updateRecord = Record.builder()
                    .id(record.getId())
                    .description(updatedDescription)
                    .member(record.getMember())
                    .build();
            Record saveRecord = recordRepository.save(updateRecord);
            return RecordDto.toDto(saveRecord);

        }
        else{
            return null;
        }
    }
    @Override
    public void deleteRecord(Long recordId){
        Optional<Record> optionalRecord = recordRepository.findById(recordId);
        optionalRecord.ifPresent(record -> {
            recordRepository.delete(record);
        });
        if (!optionalRecord.isPresent()) {
            throw new RecordNotFoundException("Record not found with id: " + recordId);
        }
    }
}
