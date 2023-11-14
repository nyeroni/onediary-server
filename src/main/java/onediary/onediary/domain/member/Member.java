package onediary.onediary.domain.member;

import jakarta.persistence.*;
import lombok.*;
import onediary.onediary.component.auditing.BaseTimeEntity;
import onediary.onediary.domain.record.Record;

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

    public void updateRecordCount(){
        this.recordCount = this.recordList.size();
    }

}
