package onediary.onediary.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import onediary.onediary.component.auditing.BaseTimeEntity;
import onediary.onediary.domain.record.Record;

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
@Table(name="member")
@Setter
public class Member extends BaseTimeEntity{

    @Id
    @Column(name = "member_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "email", columnDefinition = "VARCHAR(50)", unique = true)
    private String email;


    @Enumerated(STRING)
    @Column(name = "member_type", columnDefinition = "VARCHAR(20)", nullable = false)
    private Role role;

    @Column(name = "record_count")
    private int recordCount;

    @Column(name = "social_provider",columnDefinition = "VARCHAR(50)", nullable = false)
    @Enumerated(EnumType.STRING)
    SocialProvider socialProvider;

    private String socialId;

    private String refreshToken;

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
    public static Member create(String nickname, String email, SocialProvider socialProvider){
        return Member.builder()
                .username(nickname)
                .email(email)
                .socialProvider(socialProvider)
                .role(Role.USER)
                .build();
    }

    public static Member createApple(String email, SocialProvider socialProvider, String socialId) {
        return Member.builder()
                .username("")
                .email(email)
                .socialProvider(socialProvider)
                .role(Role.USER)
                .socialId(socialId)
                .build();
    }

   }
