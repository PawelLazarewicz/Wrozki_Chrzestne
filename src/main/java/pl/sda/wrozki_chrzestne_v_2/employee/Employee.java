package pl.sda.wrozki_chrzestne_v_2.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.wrozki_chrzestne_v_2.job.Job;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "EMPLOYEES")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private String city;

    private int age;
    private int telephoneNumber;
    private String mail;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus employeeStatus = EmployeeStatus.ACTIVE;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,}
            )
    @JoinTable(
            name = "JOBS_EMPLOYEES",
            inverseJoinColumns = {@JoinColumn(name = "WORKED_jOBS_ID")},
            joinColumns = {@JoinColumn(name = "EMPLOYEES_ID")}
    )
    private List<Job> workedJobs = new ArrayList<>();
}