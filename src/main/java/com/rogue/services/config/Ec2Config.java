package com.rogue.services.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class Ec2Config {
  private final AwsConfig awsConfig;

  @Bean
  public AmazonEC2 amazonEC2() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsConfig.getAccessKey(), awsConfig.getSecretKey());

    return AmazonEC2ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .withRegion(Regions.AP_SOUTH_1)
            .build();
  }

}
