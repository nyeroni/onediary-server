package onediary.onediary.service.impl;

import lombok.RequiredArgsConstructor;
import onediary.onediary.domain.member.Member;
import onediary.onediary.domain.member.MemberRepository;
import onediary.onediary.dto.member.MemberDto;
import onediary.onediary.service.IMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements IMemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Optional<MemberDto> findMemberById(Long memberId){
        Optional<Member> optionalMember =  memberRepository.findById(memberId);
        return optionalMember.map(MemberDto::toDto);
    }

    @Override
    @Transactional
    public Long findByEmail(String email){
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.get().getId();
    }


}
