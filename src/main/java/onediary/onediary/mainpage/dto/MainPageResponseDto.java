package onediary.onediary.mainpage.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class MainPageResponseDto {

    private MainPageMemberDetailsResponseDto memberDetailsResponseDto;
    private int diaryCount;

    public MainPageResponseDto(MainPageMemberDetailsResponseDto memberDetails, int diaryCount) {
        this.memberDetailsResponseDto = memberDetails;
        this.diaryCount = diaryCount;
    }

    public static MainPageResponseDto from(MainPageMemberDetailsResponseDto memberDetailsResponseDto, int diaryCount){
        return new MainPageResponseDto(memberDetailsResponseDto, diaryCount);
    }

    public MainPageMemberDetailsResponseDto getMemberDetails() {
        return memberDetailsResponseDto;
    }

    public int getDiaryCount() {
        return diaryCount;
    }
}

