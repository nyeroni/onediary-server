package onediary.onediary.dto.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onediary.onediary.domain.record.Record;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordViewDto {
    private Long id;
    private String description;
    private String email;
    private String emotion;
    private int currentScore;
    private LocalDate recordDate;


    static public RecordViewDto toDto(Record record){
        return RecordViewDto.builder()
                .id(record.getId())
                .currentScore(record.getCurrentScore())
                .description(record.getDescription())
                .emotion(record.getEmotion())
                .email(record.getMember().getEmail())
                .recordDate(record.getRecordDate())
                .build();
    }
}
