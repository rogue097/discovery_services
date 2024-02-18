package com.rogue.services.model;

import com.rogue.services.constants.Region;
import com.rogue.services.constants.TableName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = TableName.EC2_INSTANCES)
@NoArgsConstructor
@Getter
@Setter
public class EC2Instances extends AuditBase {
    @Id
    @Column(columnDefinition = "bigint", updatable = false, nullable = false)
    private Long id;

    @Column(name = "instance_id", columnDefinition = "varchar(255)", updatable = false, nullable = false)
    private String instanceId;

    @Column(name = "instance_type", columnDefinition = "varchar(255)", updatable = false, nullable = false)
    private String instanceType;

    @Column(name = "state", columnDefinition = "varchar(255)", updatable = false, nullable = false)
    private String state;

    @Column(name = "region",columnDefinition = "varchar", length = 50, updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(name = "job_id", columnDefinition = "bigint", updatable = false, nullable = false)
    private Long jobId;

}
