package com.rogue.services.services;

import com.rogue.services.config.AwsConfig;
import com.rogue.services.constants.Region;
import com.rogue.services.constants.Services;
import com.rogue.services.dto.S3ObjectCountDto;
import com.rogue.services.dto.ServiceDto;
import com.rogue.services.dto.projection.S3ObjectView;
import com.rogue.services.dto.projection.ServiceView;
import com.rogue.services.exceptions.DiscoveryException;
import com.rogue.services.model.S3Buckets;
import com.rogue.services.model.S3Objects;
import com.rogue.services.repository.IS3BucketsRepository;
import com.rogue.services.repository.IS3ObjectRepository;
import com.rogue.services.utility.IdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class S3Service {
  private final IS3BucketsRepository s3BucketsRepository;
  private final AwsConfig awsConfig;
  private final IS3ObjectRepository s3ObjectRepository;

  /**
   * Asynchronously discover S3 buckets and save them in the database
   * @param jobId
   * @param s3Client
   * @return
   */
  @Async("taskExecutor")
  public CompletableFuture<Void> discoveryS3Buckets(Long jobId, S3Client s3Client) {
    try {
      ListBucketsResponse listBucketsResponse = s3Client.listBuckets();
      List<Bucket> buckets = listBucketsResponse.buckets();
      List<S3Buckets> s3Buckets = new ArrayList<>();
      for (Bucket b : buckets) {
        if (s3Client.getBucketLocation(GetBucketLocationRequest.builder().bucket(b.name()).build()).locationConstraintAsString().equals(awsConfig.getRegion())) {
          S3Buckets s3Bucket = new S3Buckets();
          s3Bucket.setId(IdGenerator.getInstance().nextId());
          s3Bucket.setBucketName(b.name());
          s3Bucket.setJobId(jobId);
          s3Bucket.setRegion(Region.fromName(awsConfig.getRegion()));
          s3Bucket.setCount(count(s3Client, b.name()));
          s3Buckets.add(s3Bucket);
        }

      }
      s3BucketsRepository.saveAll(s3Buckets);
      s3Client.close();
      return CompletableFuture.completedFuture(null);
    } catch (Exception e) {
      s3Client.close();
      throw new DiscoveryException("Error while discovering S3 buckets");
    }
  }

  /**
   * Count the number of objects in a bucket
   * @param s3Client
   * @param name
   * @return
   */
  private Integer count(S3Client s3Client, String name) {
    ListObjectsV2Request request = ListObjectsV2Request.builder()
            .bucket(name)
            .build();
    ListObjectsV2Response response = s3Client.listObjectsV2(request);
    return response.keyCount();
  }

  /**
   * Get the S3 buckets from the database
   * @return
   */
  public List<ServiceDto> getS3Buckets() {
    List<ServiceView> s3Buckets = s3BucketsRepository.getLatestInstanceResult();
    List<ServiceDto> serviceDtos = new ArrayList<>();
    for (ServiceView s3Bucket : s3Buckets) {
      ServiceDto serviceDto = new ServiceDto();
      serviceDto.setServiceName(s3Bucket.getServiceName());
      serviceDto.setServiceType(Services.S3);
      serviceDtos.add(serviceDto);
    }
    return serviceDtos;
  }

  /**
   * Asynchronously get the S3 objects in a bucket and save them in the database
   * @param s3Client
   * @param bucketName
   * @param jobId
   * @return
   */
  @Async("taskExecutor")
  public CompletableFuture<Void> getS3Objects(S3Client s3Client, String bucketName, Long jobId) {
    try {
      List<S3Objects> s3ObjectsList = new ArrayList<>();
      String continuationToken = null;
      do {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .continuationToken(continuationToken)
                .build();

        ListObjectsV2Response result = s3Client.listObjectsV2(request);
        for (S3Object object : result.contents()) {
          // Add entry in S3Objects table
          S3Objects s3Object = new S3Objects();
          s3Object.setId(IdGenerator.getInstance().nextId());
          s3Object.setBucketName(bucketName);
          s3Object.setKey(object.key());
          s3Object.setSize(object.size());
          s3Object.setJobId(jobId);
          s3ObjectsList.add(s3Object);
        }

        continuationToken = result.nextContinuationToken();
      } while (continuationToken != null);

      s3ObjectRepository.saveAll(s3ObjectsList);
      // Close the S3 client
      s3Client.close();
      return CompletableFuture.completedFuture(null);
    } catch (Exception e) {
      s3Client.close();
      throw new DiscoveryException("Error while discovering S3 objects");
    }
  }

  /**
   * Get the count of S3 objects in a bucket
   * @param bucketName bucket name
   * @return S3ObjectCountDto
   */
  public S3ObjectCountDto countObjects(String bucketName) {
    S3ObjectCountDto s3ObjectCountDto = new S3ObjectCountDto();
    s3ObjectCountDto.setBucketName(bucketName);
    s3ObjectCountDto.setObjectCount(s3BucketsRepository.getLatestObjectCountByBucketName(bucketName).getCount());
    return s3ObjectCountDto;
  }

  /**
   * Get S3 objects like the pattern (regex) It uses postgres regex matching
   * @param bucketName bucket name
   * @param pattern pattern to match
   * @return list of S3ObjectView
   */
  public List<S3ObjectView> getS3BucketObjectLike(String bucketName, String pattern) {
    return s3ObjectRepository.findByBucketNameAndKeyLike(bucketName, pattern);
  }
}
