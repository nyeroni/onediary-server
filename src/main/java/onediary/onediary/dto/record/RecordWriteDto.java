package onediary.onediary.dto.record;

import lombok.*;
import onediary.onediary.domain.member.entity.Member;
import onediary.onediary.domain.record.Record;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordWriteDto {

    private String description;
    private String emotion;
    private String email;
    private LocalDate recordDate;


    public Record toEntity(Member member){

        if (this.emotion == null) {
            throw new IllegalArgumentException("Emotion cannot be null");
        }
        return Record.builder()
                .recordDate(this.recordDate)
                .description(this.description)
                .emotion(this.emotion)
                .member(member)
                .build();
    }
}
