package com.mgg.devicemanagement.model.base;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity extends BasicEntity {

    @CreatedDate
    @Column(name="created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @CreatedBy
    @Column(name = "created_by", nullable = false , updatable = false)
    private String createBy;

    @LastModifiedDate
    @Column(name="modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;
}
