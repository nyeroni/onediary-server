package onediary.onediary.member.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MemberResponseDto {


    private Long id;
    private String userName;
    private String email;
    private int recordCount;
}
