package com.rogue.services.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;

@RequiredArgsConstructor
@Configuration
public class Ec2Config {
  private final AwsConfig awsConfig;

  @Bean
  public Ec2Client ec2Client() {
    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
            awsConfig.getAccessKey(),
            awsConfig.getSecretKey());

    return Ec2Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .region(Region.of(awsConfig.getRegion()))
            .build();
  }

}
