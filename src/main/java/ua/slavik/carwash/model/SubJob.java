package ua.slavik.carwash.model;

import lombok.Builder;
import lombok.Data;
import javax.persistence.*;

@Data
@Builder
@Entity
public class SubJob
{
    @Id
    @GeneratedValue
    private Long id;

    // many to one - many subjobs can belong to one job
    @ManyToOne
    @JoinColumn(name="job_id")
    private Job job;

    // many-to-one - many subjobs can have 1 service
    @ManyToOne
    @JoinColumn(name="service_id")
    private Service service;

    // status of the individual service
    private JobStatus status;
}