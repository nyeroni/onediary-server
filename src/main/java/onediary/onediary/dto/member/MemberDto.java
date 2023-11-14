package onediary.onediary.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import onediary.onediary.domain.member.Member;

@Getter
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String email;
    private String username;

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
    }
    public static MemberDto toDto(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .build();
    }
}
