package com.rogue.services.model;

import com.rogue.services.constants.JobStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_status")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class JobStatus extends AuditBase {

  @Id
  private Long id;

  @Column(name = "status", columnDefinition = "varchar", length = 50, updatable = false, nullable = false)
  @Enumerated(EnumType.STRING)
  private JobStatusType status;

}
