package onediary.onediary.api.domain.member.repository;

import onediary.onediary.api.domain.member.entity.SocialProvider;
import onediary.onediary.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository  extends JpaRepository<Member,Long > {
    Optional<Member> findByEmail(String email);
    Member findByUserId(String userId);
    Optional<Member> findById(Long userSeqId);
}
