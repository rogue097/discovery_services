-- liquibase formatted sql


-- changeset siba:01-seed-schema-1
create table job_status
(
    id          BIGINT PRIMARY KEY NOT NULL,
    status      VARCHAR(50)        NOT NULL,
    created_at  BIGINT             NOT NULL,
    modified_at BIGINT             NOT NULL
);

-- changeset siba:01-seed-schema-2

CREATE TABLE ec2_instances
(
    id            BIGINT PRIMARY KEY NOT NULL,
    instance_id   VARCHAR(255)       NOT NULL,
    instance_type VARCHAR(255)       NOT NULL,
    state         VARCHAR(255)       NOT NULL,
    region        VARCHAR(255)       NOT NULL,
    job_id        BIGINT             NOT NULL REFERENCES job_status (id) ON DELETE CASCADE ON UPDATE CASCADE,
    created_at    BIGINT             NOT NULL,
    modified_at   BIGINT             NOT NULL
);

-- changeset siba:01-seed-schema-3
CREATE TABLE s3_buckets
(
    id          BIGINT PRIMARY KEY NOT NULL,
    bucket_name VARCHAR(255)       NOT NULL,
    job_id      BIGINT             NOT NULL REFERENCES job_status (id) ON DELETE CASCADE ON UPDATE CASCADE,
    region      VARCHAR(50)        NOT NULL,
    created_at  BIGINT             NOT NULL,
    modified_at BIGINT             NOT NULL,
    count       INTEGER            NOT NULL
);

-- changeset siba:01-seed-schema-4
CREATE TABLE s3_objects
(
    id          BIGINT PRIMARY KEY NOT NULL,
    key         VARCHAR(255)       NOT NULL,
    bucket_name VARCHAR(255)       NOT NULL,
    job_id      BIGINT             NOT NULL REFERENCES job_status (id) ON DELETE CASCADE ON UPDATE CASCADE,
    size        BIGINT             NOT NULL,
    created_at  BIGINT             NOT NULL,
    modified_at BIGINT             NOT NULL
);