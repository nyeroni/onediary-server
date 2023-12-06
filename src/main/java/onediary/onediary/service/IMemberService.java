package onediary.onediary.service;

import onediary.onediary.domain.member.entity.Member;
import onediary.onediary.dto.member.MemberResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface IMemberService {

    Member findMemberById(Long memberId);
    //MemberDto findByEmail(String email);
    int findCount(Long memberId);
  //  void logout(HttpServletRequest request);
    Member findMemberBySocialId(String socialId);
    void save(MemberResponseDto memberResponseDto);
    void update(MemberResponseDto memberResponseDto);

    @Transactional
    void updateRefreshToken(String socialId, String refreshToken);

    @Transactional
    MemberResponseDto findByRefreshToken(String refreshToken);
}
