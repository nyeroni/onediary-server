package onediary.onediary.mainpage.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class MyPageResponseDto {

    private MyPageMemberDetailsResponseDto memberDetailsResponseDto;
    private int diaryCount;

    public MyPageResponseDto(MyPageMemberDetailsResponseDto memberDetails, int diaryCount) {
        this.memberDetailsResponseDto = memberDetails;
        this.diaryCount = diaryCount;
    }

    public static MyPageResponseDto from(MyPageMemberDetailsResponseDto memberDetailsResponseDto, int diaryCount){
        return new MyPageResponseDto(memberDetailsResponseDto, diaryCount);
    }

    public MyPageMemberDetailsResponseDto getMemberDetails() {
        return memberDetailsResponseDto;
    }

    public int getDiaryCount() {
        return diaryCount;
    }
}

