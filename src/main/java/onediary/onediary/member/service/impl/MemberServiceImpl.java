package onediary.onediary.member.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.common.exception.BadRequestException;
import onediary.onediary.member.entity.Member;
import onediary.onediary.member.repository.MemberQuerydslRepository;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.record.Record;
import onediary.onediary.record.repository.RecordRepository;
import onediary.onediary.member.dto.member.MemberResponseDto;
import onediary.onediary.record.dto.record.RecordViewDto;
import onediary.onediary.oauth.service.AuthService;
import onediary.onediary.member.service.IMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberServiceImpl implements IMemberService {


    private final MemberRepository memberRepository;
    private final RecordRepository recordRepository;
    private final MemberQuerydslRepository memberQuerydslRepository;
    private final AuthService authService;

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
    @Transactional(readOnly = true)
    public List<RecordViewDto> getRecordList(String token){
        Long memberId = authService.getMemberId(token);

        List<RecordViewDto> recordViewDtos = new ArrayList<>();

        Member member = memberRepository.findByMemberId(memberId);
        for(Record record : member.getRecordList()) {
            recordViewDtos.add(RecordViewDto.toDto(record));
        }
        return recordViewDtos;

    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberResponseDto(String token){
        Long memberId = authService.getMemberId(token);
        return Optional.ofNullable(memberQuerydslRepository.findByMemberId(memberId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 정보가 존재하지 않습니다."));
    }



}
