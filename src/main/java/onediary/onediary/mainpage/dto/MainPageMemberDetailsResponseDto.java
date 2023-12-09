package onediary.onediary.mainpage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import onediary.onediary.member.domain.Member;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainPageMemberDetailsResponseDto {

    @Schema(description = "멤버 id", example = "1")
    private Long memberId;

    @Schema(description = "멤버 이름")
    private String username;

    @Schema(description = "이번 달 지금까지 작성한 일기")
    private Integer diaryCount;

    public static MainPageMemberDetailsResponseDto from (Member member){
        return MainPageMemberDetailsResponseDto.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .diaryCount(member.getRecordCount())
                .build();
    }
}
