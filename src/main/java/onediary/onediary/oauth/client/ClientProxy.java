package onediary.onediary.oauth.client;

import onediary.onediary.member.domain.Member;

public interface ClientProxy {
    Member getUserData(String accessToken);

}
