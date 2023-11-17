package onediary.onediary.dto.record.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onediary.onediary.domain.member.Member;
import onediary.onediary.domain.record.Record;

@Getter
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
