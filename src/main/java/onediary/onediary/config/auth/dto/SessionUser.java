package onediary.onediary.config.auth.dto;

import lombok.Getter;
import onediary.onediary.domain.member.Member;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;

    public SessionUser(Member member){
        this.name = member.getUsername();
        this.email = member.getEmail();
    }
}
