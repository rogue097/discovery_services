package com.rogue.services.repository;

import com.rogue.services.constants.JobStatusType;
import com.rogue.services.model.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IJobStatusRepository extends JpaRepository<JobStatus, Long> {
  @Modifying(clearAutomatically = true)
  @Transactional
  @Query(value = "UPDATE JobStatus SET status = :status WHERE id = :id")
  void updateJobStatusById(@Param("status") JobStatusType status, @Param("id") Long id);
}
