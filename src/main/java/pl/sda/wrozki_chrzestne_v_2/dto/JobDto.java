package pl.sda.wrozki_chrzestne_v_2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.wrozki_chrzestne_v_2.employee.Employee;
import pl.sda.wrozki_chrzestne_v_2.job.JobStatus;
import pl.sda.wrozki_chrzestne_v_2.job.SortOfJobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobDto {

    private Long id;

    private ClientDto client;
    private String dateOfJob;
    private String city;

        private String jobsAddress;
        private String jobsPostalCode;

    private SortOfJobs sortOfJob;
    private int estimatedTime;
    private int numberOfChildren;

    private JobStatus jobStatus = JobStatus.ACTIVE;

    private List<EmployeeDto> employees = new ArrayList<>();
}
