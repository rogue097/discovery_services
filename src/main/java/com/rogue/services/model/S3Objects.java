package com.rogue.services.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "s3_objects")
@NoArgsConstructor
@Getter
@Setter
public class S3Objects extends AuditBase {
  @Id
  @Column(columnDefinition = "bigint", updatable = false, nullable = false)
  private Long id;

  @Column(name = "bucket_name", columnDefinition = "varchar(255)", updatable = false, nullable = false)
  private String bucketName;

  @Column(name = "key", columnDefinition = "varchar(255)", updatable = false, nullable = false)
  private String key;

  @Column(name = "size", columnDefinition = "bigint", updatable = false, nullable = false)
  private Long size;

  @Column(name = "job_id", columnDefinition = "bigint", updatable = false, nullable = false)
  private Long jobId;

}
