package onediary.onediary.member.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.member.dto.member.MemberResponseDto;
import onediary.onediary.member.domain.Member;
import onediary.onediary.member.repository.MemberQuerydslRepository;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.member.service.IMemberService;
import onediary.onediary.oauth.service.AuthService;
import onediary.onediary.record.domain.Record;
import onediary.onediary.record.dto.record.RecordViewDto;
import onediary.onediary.record.repository.RecordRepository;
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

//    @Override
//    @Transactional
//    public Member findMemberBySocialId(String socialId){
//        Member member = memberQuerydslRepository.findBySocialId(socialId);
//        if (member == null) {
//            throw new BadRequestException("해당하는 멤버를 찾을 수 없습니다.");
//        }
//        return member;
//    }


    @Override
    @Transactional
    public int findCount(Long memberId){
        MemberResponseDto memberDto = memberQuerydslRepository.findByMemberId(memberId);
        return memberDto.getRecordCount();
    }





    @Override
    @Transactional(readOnly = true)
    public List<RecordViewDto> getRecordList(String token){
        Long memberId = authService.getMemberId(token);

        if (memberId == null) {
            // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            // 해당 회원이 존재하지 않는 경우
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 정보가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();


        List<RecordViewDto> recordViewDtos = new ArrayList<>();
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
