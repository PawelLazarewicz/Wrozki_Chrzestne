package pl.sda.wrozki_chrzestne_v_2.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.wrozki_chrzestne_v_2.client.Client;
import pl.sda.wrozki_chrzestne_v_2.employee.Employee;

import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "JOBS")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    private String dateOfJob;
    private String city;

    private String jobsAddress;
    private String jobsPostalCode;

    @Enumerated(EnumType.STRING)
    private SortOfJobs sortOfJob;
    private int estimatedTime;
    private int numberOfChildren;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus = JobStatus.ACTIVE;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,}
            )
    @JoinTable(
            name = "JOBS_EMPLOYEES",
            joinColumns = {@JoinColumn(name = "WORKED_jOBS_ID")},
            inverseJoinColumns = {@JoinColumn(name = "EMPLOYEES_ID")}
    )
    private List<Employee> employees = new ArrayList<>();


}
