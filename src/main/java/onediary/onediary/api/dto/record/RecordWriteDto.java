package onediary.onediary.api.dto.record;

import lombok.*;
import onediary.onediary.api.domain.member.entity.Member;
import onediary.onediary.api.domain.record.Record;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordWriteDto {
    private String description;
    private String email;

    public Record toEntity(Member member){
        return Record.builder()
                .description(this.description)
                .member(member)
                .build();
    }
}
