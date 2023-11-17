package onediary.onediary.service;

import onediary.onediary.dto.member.MemberDto;

import java.util.Optional;

public interface IMemberService {
    Optional<MemberDto> findMemberById(Long memberId);
    Long  findByEmail(String email);
    int findCount(Long memberId);
}
