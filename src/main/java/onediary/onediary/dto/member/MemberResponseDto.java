package onediary.onediary.dto.member;

import lombok.Builder;
import lombok.Data;
import onediary.onediary.domain.member.entity.Member;
import onediary.onediary.domain.member.entity.SocialProvider;

@Data
@Builder
public class MemberResponseDto {

    private String socialId;
    private String email;
    private String username;
    private SocialProvider socialProvider;
    private  String refreshToken;

    MemberResponseDto(String username, String email, String socialId, SocialProvider socialProvider, String refreshToken) {
        // 생성자 내용
    }

    public static MemberResponseDto from(Member member){
        return MemberResponseDto.builder()
                .socialId(member.getSocialId())
                .email(member.getEmail())
                .username(member.getUsername())
                .socialProvider(member.getSocialProvider())
                .build();
    }

    public MemberResponseDto(Member member){
        this(member.getSocialId(), member.getEmail(), member.getUsername(), member.getSocialProvider(), member.getRefreshToken());
    }
}
