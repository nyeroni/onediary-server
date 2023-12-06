package onediary.onediary.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import onediary.onediary.domain.member.entity.Member;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageMemberDetailsResponseDto {

    @Schema(description = "멤버 id", example = "1")
    private Long memberId;

    @Schema(description = "멤버 이름")
    private String username;

    @Schema(description = "이번 달 지금까지 작성한 일기")
    private Integer diaryCount;

    public static MyPageMemberDetailsResponseDto from (Member member){
        return MyPageMemberDetailsResponseDto.builder()
                .memberId(member.getMemberId())
                .username(member.getUsername())
                .diaryCount(member.getRecordCount())
                .build();
    }
}
