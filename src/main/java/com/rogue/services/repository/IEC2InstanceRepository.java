package com.rogue.services.repository;

import com.rogue.services.dto.projection.ServiceView;
import com.rogue.services.model.EC2Instances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IEC2InstanceRepository extends JpaRepository<EC2Instances, Long> {

  // This method is used to get the latest instance result since ids are time sorted we get the result from the latest job id
  @Query(value = "SELECT instance_id as serviceName, max(job_id) FROM ec2_instances group by instance_id", nativeQuery = true)
  List<ServiceView> getLatestInstanceResult();
}
