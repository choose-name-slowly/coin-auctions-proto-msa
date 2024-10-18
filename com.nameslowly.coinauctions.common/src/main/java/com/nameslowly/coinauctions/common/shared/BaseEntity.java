package com.nameslowly.coinauctions.common.shared;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

//    @Column(name = "created_at", updatable = false)
//    @CreatedDate
//    protected LocalDateTime createdAt;
//
//    @Column(name = "created_by", updatable = false)
//    @CreatedBy
//    protected String createdUser;
//
//    @Column(name = "updated_at")
//    @LastModifiedDate
//    protected LocalDateTime updatedAt;
//
//    @Column(name = "updated_by")
//    @LastModifiedBy
//    protected String updatedUser;
//
//    @Column(name = "deleted_at")
//    protected LocalDateTime deletedAt;
//
//    @Column(name = "deleted_by")
//    protected String deletedUser;
//
//    public void deleteEntity(String username) {
//        this.deletedAt = LocalDateTime.now();
//        this.deletedUser = username;
//    }

}
