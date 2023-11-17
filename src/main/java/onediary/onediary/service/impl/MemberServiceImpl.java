package onediary.onediary.service.impl;

import lombok.RequiredArgsConstructor;
import onediary.onediary.domain.member.Member;
import onediary.onediary.domain.member.MemberRepository;
import onediary.onediary.domain.record.RecordRepository;
import onediary.onediary.dto.member.MemberDto;
import onediary.onediary.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberServiceImpl implements IMemberService {


    private final MemberRepository memberRepository;
    private final RecordRepository recordRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, RecordRepository recordRepository) {
        this.memberRepository = memberRepository;
        this.recordRepository = recordRepository;
    }

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
