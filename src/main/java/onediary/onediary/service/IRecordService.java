package onediary.onediary.service;

import onediary.onediary.dto.record.record.RecordDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IRecordService {
    List<RecordDto> findAllRecords(String email);
    Optional<RecordDto> findRecordById(Long recordId);
    Optional<RecordDto> findRecordByCreatedDate(LocalDateTime createdDate);
    RecordDto createRecord(RecordDto recordDto);
    RecordDto updateRecordDescription(Long recordId, String updateDescription);
    void deleteRecord(Long recordId);
}
