package onediary.onediary.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import onediary.onediary.domain.member.entity.Member;
import onediary.onediary.domain.member.entity.QMember;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MemberQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional(readOnly = true)
    public Member findBySocialId(String socialId) {
        return jpaQueryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.socialId.eq(socialId))
                .fetchOne();
    }
    @Transactional(readOnly = true)
    public Member findByRefreshToken(String refreshToken) {
        return jpaQueryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.refreshToken.eq(refreshToken))
                .fetchOne();
    }

//    public UserInfoResponse findByMemberId(Long memberId) {
//        return jpaQueryFactory
//                .select(Projections.fields(UserInfoResponse.class,
//                        member.username.as("nickName"),
//                        member.email))
//                .from(member)
//                .where(member.memberId.eq(memberId))
//                .fetchOne();
//    }

}