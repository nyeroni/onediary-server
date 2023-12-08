package onediary.onediary.record;

import jakarta.persistence.*;
import lombok.*;
import onediary.onediary.member.entity.Member;
import onediary.onediary.component.auditing.BaseEntity;

import java.time.LocalDate;

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

    @Column(columnDefinition = "TEXT", length = 10, nullable = false)
    private String emotion;

    @Column(columnDefinition = "TEXT", length = 40, nullable = false)
    private String description;

    @Column(name = "current_score")
    private int currentScore;

    @Column(name = "record_date")
    private LocalDate recordDate;


    /*
    user의 현재까지 score
     */
    public int getCurrentScore(Member member){
        return member.getRecordCount();
    }
    public void setMember(Member member){
        this.member = member;
    }
    public void updateEmotion(String emotion){
        this.emotion = emotion;
    }
    public void updateDescription(String description){

        this.description = description;
    }

    public LocalDate getRecordDate(){
        return recordDate;
    }
    public void setRecordDate(LocalDate recordDate){
        this.recordDate = recordDate;
    }


}
