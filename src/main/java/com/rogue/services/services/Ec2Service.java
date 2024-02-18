package com.rogue.services.services;

import com.rogue.services.config.AwsConfig;
import com.rogue.services.config.Ec2Config;
import com.rogue.services.constants.Region;
import com.rogue.services.constants.Services;
import com.rogue.services.dto.ServiceDto;
import com.rogue.services.dto.projection.ServiceView;
import com.rogue.services.exceptions.DiscoveryException;
import com.rogue.services.model.EC2Instances;
import com.rogue.services.repository.IEC2InstanceRepository;
import com.rogue.services.utility.IdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class Ec2Service {
  private final Ec2Config ec2Config;
  private final IEC2InstanceRepository ec2InstanceRepository;
  private final AwsConfig awsConfig;

  /**
   * Asynchronously discover EC2 instances and save them in the database
   * @param jobId
   * @return
   */
  @Async("taskExecutor")
  public CompletableFuture<Void> discoveryEC2Instances(Long jobId) {
    try {
      System.out.println("Discovering EC2 instances" + Thread.currentThread().getName());
      Ec2Client ec2Client = ec2Config.ec2Client();

      String nextToken = null;
      List<EC2Instances> ec2Instances = new ArrayList<>();
      do {
        DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                .nextToken(nextToken)
                .build();
        DescribeInstancesResponse response = ec2Client.describeInstances(request);

        for (Reservation reservation : response.reservations()) {
          for (Instance instance : reservation.instances()) {
            EC2Instances ec2Instance = new EC2Instances();
            ec2Instance.setId(IdGenerator.getInstance().nextId());
            ec2Instance.setInstanceId(instance.instanceId());
            ec2Instance.setInstanceType(instance.instanceType().toString());
            ec2Instance.setState(instance.state().nameAsString());
            ec2Instance.setJobId(jobId);
            ec2Instance.setRegion(Region.fromName(awsConfig.getRegion()));
            ec2Instances.add(ec2Instance);
          }
        }

        nextToken = response.nextToken();
      } while (nextToken != null);

      ec2InstanceRepository.saveAll(ec2Instances);
      return CompletableFuture.completedFuture(null);

    } catch (Exception e) {

      throw new DiscoveryException("Error while discovering EC2 instances");
    }
  }

  /**
   * Get the latest EC2 instances
   * @return
   */
  public List<ServiceDto> getEC2Instances() {
    List<ServiceView> ec2Instances = ec2InstanceRepository.getLatestInstanceResult();
    List<ServiceDto> serviceDtos = new ArrayList<>();
    for (ServiceView serviceView : ec2Instances) {
      ServiceDto serviceDto = new ServiceDto();
      serviceDto.setServiceName(serviceView.getServiceName());
      serviceDto.setServiceType(Services.EC2);
      serviceDtos.add(serviceDto);
    }
    return serviceDtos;
  }
}
