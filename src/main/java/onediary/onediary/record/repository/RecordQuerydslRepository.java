package onediary.onediary.record.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import onediary.onediary.record.domain.Record;
import org.springframework.stereotype.Repository;

import static onediary.onediary.record.domain.QRecord.record;


@Repository
@RequiredArgsConstructor
public class RecordQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Record findByRecordIdAndMemberId(Long recordId, Long memberId){
        return jpaQueryFactory
                .selectFrom(record)
                .where(record.id.eq(recordId).and(record.member.id.eq(memberId)))
                .fetchOne();


    }
//    public Record findByMemberId(Long memberId){
//        return jpaQueryFactory
//                .selectFrom(record)
//                .where(record.member.id.eq(memberId))
//                .fetchOne();
//    }
}
