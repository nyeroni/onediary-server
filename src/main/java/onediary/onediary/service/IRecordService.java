package onediary.onediary.service;

import onediary.onediary.dto.record.record.RecordViewDto;
import onediary.onediary.dto.record.record.RecordWriteDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IRecordService {
    List<RecordViewDto> getRecords(Long memberId);
    List<RecordViewDto> findAllRecords(String email);
    RecordViewDto findRecordById(Long recordId);
    Optional<Long> findRecordByCreatedDate(LocalDateTime createdDate);
    Long createRecord(RecordWriteDto recordDto);

    Long updateRecordDescription(Long recordId, String updatedDescription);
    void deleteRecord(Long recordId);
    List<RecordViewDto> getRecordsAfterUpdate(Long memberId);
}
