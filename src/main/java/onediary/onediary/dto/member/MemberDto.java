package onediary.onediary.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import onediary.onediary.domain.member.Member;
import onediary.onediary.domain.member.SocialProvider;

@Getter
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String email;
    private String username;
    private SocialProvider socialProvider;

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .username(username)
                .email(email)
                .socialProvider(socialProvider)
                .build();
    }
    public static MemberDto toDto(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .socialProvider(member.getSocialProvider())
                .build();
    }
}
