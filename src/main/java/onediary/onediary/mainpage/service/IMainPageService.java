package onediary.onediary.mainpage.service;

import onediary.onediary.mainpage.dto.MainPageMemberDetailsResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface IMainPageService {
    @Transactional(readOnly = true)
    MainPageMemberDetailsResponseDto getMainPageDetail(String token);
}
