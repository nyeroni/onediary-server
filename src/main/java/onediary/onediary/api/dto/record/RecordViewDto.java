package onediary.onediary.api.dto.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onediary.onediary.api.domain.record.Record;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordViewDto {
    private Long id;
    private String description;
    private String email;

    static public RecordViewDto toDto(Record record){
        return RecordViewDto.builder()
                .id(record.getId())
                .description(record.getDescription())
                .email(record.getMember().getEmail())
                .build();
    }
}
