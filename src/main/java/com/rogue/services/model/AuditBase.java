package com.rogue.services.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rogue.services.utility.DateTimeUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public class AuditBase implements Serializable {
    @Serial
    private static final long serialVersionUID = -4489966120680536718L;

    @JsonIgnore
    @Column(columnDefinition = "bigint", updatable = false, nullable = false)
    private Long createdAt;

    @JsonIgnore
    @Column(columnDefinition = "bigint", nullable = false)
    private Long modifiedAt;

    @PrePersist
    public void beforePersist() {
        createdAt = DateTimeUtils.now();
        modifiedAt = DateTimeUtils.now();
    }

    @PreUpdate
    public void beforeUpdate() {
        modifiedAt = DateTimeUtils.now();
    }
}
