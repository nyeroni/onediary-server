package onediary.onediary.oauth.client;

import onediary.onediary.member.entity.Member;

public interface ClientProxy {
    Member getUserData(String accessToken);

}
