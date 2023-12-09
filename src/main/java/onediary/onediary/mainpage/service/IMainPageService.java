package onediary.onediary.mainpage.service;

import onediary.onediary.mainpage.dto.MyPageMemberDetailsResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface IMainPageService {
    @Transactional(readOnly = true)
    MyPageMemberDetailsResponseDto getMainPageDetail(String token);
}
