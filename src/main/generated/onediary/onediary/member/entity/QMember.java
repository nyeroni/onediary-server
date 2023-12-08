package onediary.onediary.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1166214671L;

    public static final QMember member = new QMember("member1");

    public final onediary.onediary.component.auditing.QBaseTimeEntity _super = new onediary.onediary.component.auditing.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> recordCount = createNumber("recordCount", Integer.class);

    public final ListPath<onediary.onediary.record.Record, onediary.onediary.record.QRecord> recordList = this.<onediary.onediary.record.Record, onediary.onediary.record.QRecord>createList("recordList", onediary.onediary.record.Record.class, onediary.onediary.record.QRecord.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath socialId = createString("socialId");

    public final EnumPath<SocialProvider> socialProvider = createEnum("socialProvider", SocialProvider.class);

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

