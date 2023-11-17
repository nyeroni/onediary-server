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
    public Optional<MemberDto> findMemberById(Long memberId){
        Optional<Member> optionalMember =  memberRepository.findById(memberId);
        return optionalMember.map(MemberDto::toDto);
    }

    @Override
    public Long findByEmail(String email){
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.get().getId();
    }

    @Override
    @Transactional
    public int countRecordByMember(Long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.updateRecordCount(); // 레코드 카운트 업데이트
            return member.getRecordCount();
        } else {
            throw new IllegalArgumentException("Member not found");
        }
    }

}
