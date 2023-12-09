package onediary.onediary.member.domain;

import jakarta.persistence.*;
import lombok.*;
import onediary.onediary.component.auditing.BaseTimeEntity;
import onediary.onediary.record.domain.Record;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="MEMBER")
@Setter
public class Member extends BaseTimeEntity{

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "email", columnDefinition = "VARCHAR(50)", unique = true)
    private String email;


    @Enumerated(STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "record_count")
    private int recordCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    SocialProvider socialProvider;

    @Column(nullable = false)
    private String socialId;


    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<Record> recordList = new ArrayList<>();



    public void addRecord(Record record) {
        recordList.add(record);
        record.setMember(this);
    }


    public int getRecordCount() {

        return recordList.size();
    }

    public void resetCount(){
        LocalDateTime today = LocalDateTime.now();
        if(today.getDayOfMonth() == 1){
            this.recordCount = 0;
        }
    }

}
