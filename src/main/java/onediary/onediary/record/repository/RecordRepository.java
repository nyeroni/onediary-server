package onediary.onediary.record.repository;

import onediary.onediary.record.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    //List<Record> findAllByMemberId(Long memberId);
    @Query("SELECT r FROM Record r WHERE DATE(r.recordDate) = DATE(:recordDate)")
    Optional<Record> findByCreatedDate(@Param("recordDate") LocalDate recordDate);
}

