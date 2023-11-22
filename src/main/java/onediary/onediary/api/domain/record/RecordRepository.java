package onediary.onediary.api.domain.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("SELECT r FROM Record r WHERE r.member.email = :email")
    List<Record> findAllByEmail(@Param("email") String email);
    @Query("SELECT r FROM Record r WHERE DATE(r.createdDate) = DATE(:createdDate)")
    Optional<Record> findByCreatedDate(@Param("createdDate") LocalDateTime createdDate);

}
