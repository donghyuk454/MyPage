package com.mong.project.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_date_time", nullable = false, updatable = false)
    protected LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "last_modified_date_time", nullable = false)
    protected LocalDateTime lastModifiedDateTime;

    @Column(name = "deleted_date_time", updatable = false)
    protected LocalDateTime deletedDateTime;
}
