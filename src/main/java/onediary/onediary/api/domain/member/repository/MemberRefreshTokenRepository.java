package onediary.onediary.api.domain.member.repository;

import onediary.onediary.api.domain.member.entity.MemberRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {
    MemberRefreshToken findByUserId(String userId);
    MemberRefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}
