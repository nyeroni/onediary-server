package onediary.onediary.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.common.exception.BadRequestException;
import onediary.onediary.domain.member.entity.Member;
import onediary.onediary.domain.member.repository.MemberQuerydslRepository;
import onediary.onediary.domain.member.repository.MemberRepository;
import onediary.onediary.domain.record.RecordRepository;
import onediary.onediary.dto.member.MemberResponseDto;
import onediary.onediary.service.IMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberServiceImpl implements IMemberService {


    private final MemberRepository memberRepository;
    private final RecordRepository recordRepository;
    private final MemberQuerydslRepository memberQuerydslRepository;
    @Override
    @Transactional
    public Member findMemberBySocialId(String socialId){
        Member member = memberQuerydslRepository.findBySocialId(socialId);
        if (member == null) {
            throw new BadRequestException("해당하는 멤버를 찾을 수 없습니다.");
        }
        return member;
    }

    //내 정보 조회
    @Override
    @Transactional
    public Member findMemberById(Long memberId){

        Member member = memberRepository.findById(memberId).get();
        if (member == null) {
            throw new BadRequestException("해당하는 멤버를 찾을 수 없습니다.");
        }
        return member;
    }


    @Override
    @Transactional
    public int findCount(Long memberId){
        Member member = memberRepository.findById(memberId).get();
        return member.getRecordCount();
    }

    @Override
    @Transactional
    public void update(MemberResponseDto memberResponseDto){
        Member member = memberQuerydslRepository.findBySocialId(memberResponseDto.getSocialId());
        member.setEmail(memberResponseDto.getEmail());
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void save(MemberResponseDto memberResponseDto){
        Member member = memberQuerydslRepository.findBySocialId(memberResponseDto.getSocialId());
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void updateRefreshToken(String socialId, String refreshToken){
        Member member = memberQuerydslRepository.findBySocialId(socialId);
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public MemberResponseDto findByRefreshToken(String refreshToken) {
        Member member = memberQuerydslRepository.findByRefreshToken(refreshToken);
        if (member == null) {
            return null;
        }
        return new MemberResponseDto(member);
    }


}
