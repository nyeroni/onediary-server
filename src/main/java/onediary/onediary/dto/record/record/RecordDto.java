package onediary.onediary.dto.record.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import onediary.onediary.domain.record.Record;

@Getter
@AllArgsConstructor
@Builder
public class RecordDto {
    private Long id;
    private String description;
    public Record toEntity(){
        return Record.builder()
                .id(id)
                .description(description)
                .build();
    }
    public static RecordDto toDto(Record record){
        return RecordDto.builder()
                .id(record.getId())
                .description(record.getDescription())
                .build();
    }
}
