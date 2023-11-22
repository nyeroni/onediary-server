package onediary.onediary.api.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import onediary.onediary.api.component.auditing.BaseTimeEntity;
import onediary.onediary.api.domain.member.repository.MemberRepository;
import onediary.onediary.api.domain.record.Record;

import java.time.LocalDateTime;
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
@Setter
public class Member extends BaseTimeEntity{

    @JsonIgnore
    @Id
    @Column(name = "USER_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(name = "USER_ID", length = 64, unique = true, nullable = false)
    @Size(max = 64)
    private String userId;

    @Column(name = "USERNAME", length = 100, nullable = false)
    @Size(max = 100)
    private String username;

    @Column(name = "EMAIL", length = 512, unique = true, nullable = false)
    @Size(max = 512)
    private String email;

    @Column(name = "EMAIL_VERIFIED_YN", length = 1, nullable = false)
    @Size(min = 1, max = 1)
    private String emailVerifiedYn;

    @JsonIgnore
    @Column(name = "PASSWORD", length = 128, nullable = false)
    @Size(max = 128)
    private String password;

    @Builder.Default
    @Enumerated(STRING)
    @Column(name = "ROLE",nullable = false)
    private Role role = Role.GUEST;

    @Column(name = "RECORD_COUNT")
    private int recordCount;

    @Column(name = "SOCIAL_PROVIDER", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull    private SocialProvider socialProvider;




    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<Record> recordList = new ArrayList<>();

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT", nullable = false)
    private LocalDateTime modifiedAt;


    public void updateRecordCount() {

        this.recordCount = this.recordList.size();
    }

    public void addRecord(Record record) {
        recordList.add(record);
        record.setMember(this);
        updateRecordCount();
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

    public void resetCount(){
        LocalDateTime today = LocalDateTime.now();
        if(today.getDayOfMonth() == 1){
            this.recordCount = 0;
        }
    }
    public Member(
            @NotNull @Size(max = 64) String userId,
            @Size(max = 100)String username,
            @NotNull @Size(max = 512) String email,
            @NotNull @Size(max = 1) String emailVerifiedYn,
            @NotNull  SocialProvider socialProvider,
            @NotNull Role role,
            @NotNull LocalDateTime createdAt,
            @NotNull LocalDateTime modifiedAt

            ){
        this.userId = userId;
        this.username = username;
        this.password = "NO_PASS";
        this.email = email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn;
        this. socialProvider = socialProvider;
        this.role = role;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

   }
