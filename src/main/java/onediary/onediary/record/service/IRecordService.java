package onediary.onediary.record.service;

import onediary.onediary.record.dto.record.RecordViewDto;
import onediary.onediary.record.dto.record.RecordWriteDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface IRecordService {

    @Transactional(readOnly = true)
    RecordViewDto findRecordById(Long recordId);

    @Transactional(readOnly = true)
    RecordViewDto findRecordByCreatedDate(LocalDate createdDate);

    @Transactional
    void createRecord(String token, RecordWriteDto recordDto);

    @Transactional
    void updateRecordEmotion(Long recordId, String updatedEmotion);

    @Transactional
    void updateRecordDescription(Long recordId, String updatedDescription);

    @Transactional
    void deleteRecord(String token, Long recordId);
}
