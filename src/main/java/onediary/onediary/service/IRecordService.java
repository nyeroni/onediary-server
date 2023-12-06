package onediary.onediary.service;

import onediary.onediary.dto.record.RecordViewDto;
import onediary.onediary.dto.record.RecordWriteDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRecordService {
    List<RecordViewDto> findAllRecordsByMemberId(Long memberId);
    RecordViewDto findRecordById(Long recordId);
    Optional<RecordViewDto> findRecordByCreatedDate(LocalDate createdDate);
    RecordViewDto createRecord(RecordWriteDto recordDto);

    RecordViewDto updateRecordDescription(Long recordId, String updatedDescription);
    RecordViewDto updateRecordEmotion(Long recordId, String updatedEmotion);

    void deleteRecord(Long recordId);
}
