package com.rogue.services.model;

import com.rogue.services.constants.Region;
import com.rogue.services.constants.TableName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = TableName.S3_BUCKETS)
@NoArgsConstructor
@Getter
@Setter
public class S3Buckets extends AuditBase {
    @Id
    @Column(columnDefinition = "bigint", updatable = false, nullable = false)
    private Long id;

    @Column(name = "bucket_name", columnDefinition = "varchar(255)", updatable = false, nullable = false)
    private String bucketName;

    @Column(name = "job_id", columnDefinition = "bigint", updatable = false, nullable = false)
    private Long jobId;

    @Column(name = "region", columnDefinition = "varchar", length = 50, updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(name = "count", columnDefinition = "integer", updatable = false, nullable = false)
    private Integer count;
}
