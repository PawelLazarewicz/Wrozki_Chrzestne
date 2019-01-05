package pl.sda.wrozki_chrzestne_v_2.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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

    private String clientName;
    private String clientLastName;
    private Date dateOfJob;
    private String city;

        private String jobsAddress;
        private String jobsPostalCode;

    @Enumerated(EnumType.STRING)
    private SortOfJobs sortOfJob;
    private int estimatedTime;
    private int numberOfChildren;

}
