package onediary.onediary.domain.member;

import jakarta.persistence.*;
import lombok.*;
import onediary.onediary.component.auditing.BaseTimeEntity;
import onediary.onediary.domain.record.Record;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String email;

    private int recordCount;

    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;


    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<Record> recordList = new ArrayList<>();

    public void updateRecordCount() {
        this.recordCount = this.recordList.size();
    }

    public void addRecord(Record record) {
        recordList.add(record);
        record.setMember(this);
    }

    public int getRecordCount() {
        return recordList.size();
    }
    public void save(MemberRepository memberRepository){

        memberRepository.save(this);

    }
    public void resetCount(){
        LocalDateTime today = LocalDateTime.now();
        if(today.getDayOfMonth() == 1){
            this.recordCount = 0;
        }
    }

}
