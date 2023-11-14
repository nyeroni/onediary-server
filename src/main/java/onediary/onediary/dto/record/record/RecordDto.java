package onediary.onediary.dto.record.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import onediary.onediary.domain.member.Member;
import onediary.onediary.domain.record.Record;

@Getter
@AllArgsConstructor
@Builder
public class RecordDto {
    private Long id;
    private String description;
    private Member member;

    public Record toEntity(){
        return Record.builder()
                .id(id)
                .description(description)
                .member(member)
                .build();
    }
    public static RecordDto toDto(Record record){
        return RecordDto.builder()
                .id(record.getId())
                .description(record.getDescription())
                .member(record.getMember())
                .build();
    }
}
