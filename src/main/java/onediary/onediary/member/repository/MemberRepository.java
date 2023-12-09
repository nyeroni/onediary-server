package onediary.onediary.member.repository;

import onediary.onediary.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Override
    Optional<Member> findById(Long memberId);

    //    @Query("select m from Member m where m.email =:email")
       Optional<Member> findByEmail( String email);
//

}
