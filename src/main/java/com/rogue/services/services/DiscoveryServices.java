package com.rogue.services.services;


import com.rogue.services.config.S3Config;
import com.rogue.services.constants.JobStatusType;
import com.rogue.services.dto.*;
import com.rogue.services.dto.projection.S3ObjectView;
import com.rogue.services.exceptions.DiscoveryException;
import com.rogue.services.model.JobStatus;
import com.rogue.services.repository.IJobStatusRepository;
import com.rogue.services.utility.IdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class DiscoveryServices {
  private final Ec2Service ec2Service;
  private final IJobStatusRepository jobStatusRepository;
  private final S3Service s3Service;
  private final S3Config s3Config;

  /**
   * this method gets latest instance result and push the asynchronously to the database
   * @param updateServiceRequestDto contains the services to be updated
   * @return ProblemDetail
   */
  public ProblemDetail updateServices(UpdateServiceRequestDto updateServiceRequestDto) {
    JobStatusType jobStatusType = JobStatusType.NOT_STARTED;
    Long jobId = IdGenerator.getInstance().nextId();

    JobStatus jobStatus = new JobStatus();
    jobStatus.setId(jobId);
    jobStatus.setStatus(jobStatusType);
    jobStatusRepository.save(jobStatus);


    List<CompletableFuture<Void>> futures = new ArrayList<>();

    for (String service : updateServiceRequestDto.getServices()) {
      CompletableFuture<Void> future;
      switch (service) {
        case "EC2":
          future = ec2Service.discoveryEC2Instances(jobId);
          futures.add(future);
          break;
        case "S3":
          future = s3Service.discoveryS3Buckets(jobId, s3Config.s3Client());
          futures.add(future);
          break;
        default: {
          jobStatusRepository.updateJobStatusById(JobStatusType.FAILED, jobId);
          throw new DiscoveryException("Invalid service type: %s".formatted(service));
        }
      }
    }
    jobStatusRepository.updateJobStatusById(JobStatusType.IN_PROGRESS, jobId);

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> jobStatusRepository.updateJobStatusById(JobStatusType.COMPLETED, jobId))
            .exceptionally(ex -> {
              jobStatusRepository.updateJobStatusById(JobStatusType.FAILED, jobId);
              return null;
            });


    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(200), "Job Id: %s".formatted(jobId));
    problemDetail.setTitle("Job submitted successfully");
    return problemDetail;
  }

  /**
   * this method gets the job status
   * @param jobId the job id
   * @return JobStatusDto
   */
  public JobStatusDto getJobResult(Long jobId) {
    JobStatus jobStatus = jobStatusRepository.findById(jobId).orElse(null);
    if (jobStatus == null) {
      throw new DiscoveryException("Job status not found for id: %s".formatted(jobId));
    }
    JobStatusDto jobStatusDto = new JobStatusDto();
    jobStatusDto.setId(jobStatus.getId());
    jobStatusDto.setStatus(jobStatus.getStatus());
    return jobStatusDto;
  }

  /**
   * this method gets the latest instance result
   * @param service the service type
   * @return List<ServiceDto>
   */
  public List<ServiceDto> getDiscoveryResult(String service) {
    return switch (service) {
      case "EC2" -> ec2Service.getEC2Instances();
      case "S3" -> s3Service.getS3Buckets();
      default -> throw new DiscoveryException("Invalid service type: %s".formatted(service));
    };
  }

  /**
   * this method pushes the objects in a bucket asynchronously to the database and returns with a job id
   * @param bucketName
   * @return
   */
  public ProblemDetail getObjects(String bucketName) {
    JobStatusType jobStatusType = JobStatusType.NOT_STARTED;
    Long jobId = IdGenerator.getInstance().nextId();

    JobStatus jobStatus = new JobStatus();
    jobStatus.setId(jobId);
    jobStatus.setStatus(jobStatusType);
    jobStatusRepository.save(jobStatus);

    CompletableFuture<Void> future = s3Service.getS3Objects(s3Config.s3Client(), bucketName, jobId);
    jobStatusRepository.updateJobStatusById(JobStatusType.IN_PROGRESS, jobId);

    future.thenRun(() -> jobStatusRepository.updateJobStatusById(JobStatusType.COMPLETED, jobId))
            .exceptionally(ex -> {
              jobStatusRepository.updateJobStatusById(JobStatusType.FAILED, jobId);
              return null;
            });

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(200), "Job Id: %s".formatted(jobId));
    problemDetail.setTitle("Job submitted successfully");
    return problemDetail;
  }

  /**
   * this method counts the number of objects in a bucket
   * @param bucketName
   * @return
   */
  public S3ObjectCountDto countObjects(String bucketName) {
    return s3Service.countObjects(bucketName);
  }

  /**
   * this method gets the objects in a bucket that match a pattern
   * @param bucketName
   * @param pattern
   * @return
   */
  public List<S3ObjectView> getObjectsLike(String bucketName, String pattern) {
    return s3Service.getS3BucketObjectLike(bucketName, pattern);
  }


}
