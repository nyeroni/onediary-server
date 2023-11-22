package onediary.onediary.api.domain.record;

import jakarta.persistence.*;
import lombok.*;
import onediary.onediary.api.domain.member.entity.Member;
import onediary.onediary.api.component.auditing.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Record extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT", length = 500, nullable = false)
    private String description;
    public void setMember(Member member){
        this.member = member;
        member.addRecord(this);
    }
    public void update(String description){
        this.description = description;
    }
}
