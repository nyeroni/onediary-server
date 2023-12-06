package onediary.onediary.domain.member.repository;

import onediary.onediary.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(Long memberId);
//    @Query("select m from Member m where m.email =:email")
       Optional<Member> findByEmail( String email);
//

}
