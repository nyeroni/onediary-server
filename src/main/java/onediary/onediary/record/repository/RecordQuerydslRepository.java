package onediary.onediary.record.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import onediary.onediary.record.Record;
import org.springframework.stereotype.Repository;

import static onediary.onediary.record.QRecord.record;


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
}
