package com.rogue.services.repository;

import com.rogue.services.dto.projection.ServiceView;
import com.rogue.services.model.S3Buckets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IS3BucketsRepository extends JpaRepository<S3Buckets, Long> {
  // This method is used to get the latest instance result since ids are time sorted we get the result from the latest job id
  @Query(value = "SELECT bucket_name as serviceName, max(job_id) FROM s3_buckets group by bucket_name", nativeQuery = true)
  List<ServiceView> getLatestInstanceResult();

  @Query(value = "SELECT count as count, max(job_id) FROM s3_buckets where bucket_name = :bucketName group by count", nativeQuery = true)
  ServiceView getLatestObjectCountByBucketName(String bucketName);
}
