package com.rogue.services.repository;

import com.rogue.services.dto.projection.S3ObjectView;
import com.rogue.services.model.S3Objects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IS3ObjectRepository extends JpaRepository<S3Objects, Long> {

  @Query(value = "SELECT key, bucket_name as bucketName, max(job_id) from public.s3_objects where key ~ :pattern and bucket_name = :bucketName group by key, bucket_name", nativeQuery = true)
  List<S3ObjectView> findByBucketNameAndKeyLike(String bucketName, String pattern);
}
