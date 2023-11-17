package onediary.onediary.domain.member;

import jakarta.persistence.*;
import lombok.*;
import onediary.onediary.component.auditing.BaseTimeEntity;
import onediary.onediary.domain.record.Record;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;

@ToString(of = {"id", "username", "email", "role", "socialProvider"})
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

    @Builder.Default
    @Enumerated(STRING)
    private Role role = Role.GUEST;

    private int recordCount;

    @Enumerated(STRING)
    private SocialProvider socialProvider;


    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<Record> recordList = new ArrayList<>();

    public void updateRecordCount() {
        this.recordCount = this.recordList.size();
    }

    public void addRecord(Record record) {
        System.out.println("OKKKKK");
        recordList.add(record);
        record.setMember(this);
    }

    public int getRecordCount() {
        return recordList.size();
    }
    public void save(MemberRepository memberRepository){

        memberRepository.save(this);

    }
    public void updateRoleKey(Role role) {
        this.role = role;
    }

}
