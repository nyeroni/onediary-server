package onediary.onediary.member.service;

import onediary.onediary.member.entity.Member;
import onediary.onediary.member.dto.member.MemberResponseDto;
import onediary.onediary.record.dto.record.RecordViewDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMemberService {


    @Transactional
    Member findMemberBySocialId(String socialId);

    Member findMemberById(Long memberId);
    int findCount(Long memberId);

    @Transactional(readOnly = true)
    List<RecordViewDto> getRecordList(String token);

    @Transactional(readOnly = true)
    MemberResponseDto getMemberResponseDto(String token);
}
