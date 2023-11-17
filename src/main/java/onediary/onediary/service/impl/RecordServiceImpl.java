package onediary.onediary.service.impl;

import lombok.RequiredArgsConstructor;
import onediary.onediary.domain.member.Member;
import onediary.onediary.domain.member.MemberRepository;
import onediary.onediary.domain.record.Record;
import onediary.onediary.domain.record.RecordRepository;
import onediary.onediary.dto.record.record.RecordViewDto;
import onediary.onediary.dto.record.record.RecordWriteDto;
import onediary.onediary.service.IRecordService;
import onediary.onediary.service.exception.RecordNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements IRecordService {
    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public List<RecordViewDto> getRecords(Long memberId){
        Member member = memberRepository.findById(memberId).get();
        List<Record> recordList = recordRepository.findAllByEmail(member.getEmail());

        List<RecordViewDto> recordViewDtos = new ArrayList<>();
        for(Record record : recordList){
            recordViewDtos.add(RecordViewDto.toDto(record));
        }
        return recordViewDtos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<RecordViewDto> findAllRecords(String email){
        List<Record> recordList = recordRepository.findAllByEmail(email);
        return recordList.stream().map(RecordViewDto::toDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    @Override
    public RecordViewDto findRecordById(Long recordId) {
        Optional<Record> optionalRecord = recordRepository.findById(recordId);

        return optionalRecord.map(RecordViewDto::toDto).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Long> findRecordByCreatedDate(LocalDateTime createdDate) {
        System.out.println("createdDate="+createdDate);
        Optional<Record> recordOptional = recordRepository.findByCreatedDate(createdDate);

        return recordOptional.map(Record::getId);
    }
    @Transactional
    @Override
    public Long createRecord(RecordWriteDto recordDto){
        String email = recordDto.getEmail();
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new NoSuchElementException("Member not found with email: " + email));

        Record record = recordDto.toEntity(member);
        member.addRecord(record);
        recordRepository.save(record);

        return record.getId();
    }
    @Transactional
    @Override
    public Long updateRecordDescription(Long recordId, String updatedDescription){
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기가 없습니다. id=" + recordId));
        System.out.println("record==="+record.getDescription());

        record.update(updatedDescription);
        System.out.println("record==="+record.getDescription());


        return recordId;

    }
    @Transactional
    @Override
    public void deleteRecord(Long recordId){
        Optional<Record> optionalRecord = recordRepository.findById(recordId);

        if (!optionalRecord.isPresent()) {
            throw new RecordNotFoundException("Record not found with id: " + recordId);
        }
        optionalRecord.ifPresent(record -> {
            recordRepository.delete(record);
        });
    }
    @Override
    public List<RecordViewDto> getRecordsAfterUpdate(Long memberId) {
        return getRecords(memberId);
    }
}
