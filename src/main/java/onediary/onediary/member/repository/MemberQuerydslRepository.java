package onediary.onediary.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import onediary.onediary.member.domain.Member;
import onediary.onediary.member.dto.member.MemberResponseDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static onediary.onediary.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Transactional(readOnly = true)
    public Member findBySocialId(String socialId) {
        return jpaQueryFactory
                .selectFrom(member)
                .where(member.socialId.eq(socialId))
                .fetchOne();
    }

    public MemberResponseDto findByMemberId(Long memberId) {
        return jpaQueryFactory
                .select(Projections.fields(MemberResponseDto.class,
                        member.username.as("nickName"),
                        member.email))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne();
    }


}