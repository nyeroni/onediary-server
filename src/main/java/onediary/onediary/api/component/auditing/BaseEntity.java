package onediary.onediary.api.component.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends BaseTimeEntity{

    @CreatedBy
    @Column(updatable = false)
    private String createBy;

    @LastModifiedBy
    private String lastModifiedBy;

}
