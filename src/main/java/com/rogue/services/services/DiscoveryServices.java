package com.rogue.services.services;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.model.Bucket;
import com.rogue.services.config.Ec2Config;
import com.rogue.services.config.S3Config;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DiscoveryServices {
  private final Ec2Config ec2Config;
  private final S3Config s3Config;

  public void discoveryS3Buckets() {
    List<Bucket> buckets = s3Config.amazonS3().listBuckets();
    for (Bucket b : buckets) {
      System.out.println(b.getName());
    }
  }

  public void discoveryEC2Instances() {
    AmazonEC2 ec2Client = ec2Config.amazonEC2();

    DescribeInstancesRequest request = new DescribeInstancesRequest();
    do {
      DescribeInstancesResult response = ec2Client.describeInstances(request);

      for (Reservation reservation : response.getReservations()) {
        for (Instance instance : reservation.getInstances()) {
          System.out.printf(
                  "Found reservation with id %s, " +
                          "AMI %s, " +
                          "type %s, " +
                          "state %s " +
                          "and monitoring state %s\n",
                  instance.getInstanceId(),
                  instance.getImageId(),
                  instance.getInstanceType(),
                  instance.getState().getName(),
                  instance.getMonitoring().getState()
          );
        }
      }

      request.setNextToken(response.getNextToken());
    } while (request.getNextToken() != null);
  }
}
