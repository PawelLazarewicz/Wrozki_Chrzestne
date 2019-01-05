package pl.sda.wrozki_chrzestne_v_2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.wrozki_chrzestne_v_2.job.SortOfJobs;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobDto {

    private String clientName;
    private String clientLastName;
    private Date dateOfJob;
    private String city;

        private String jobsAddress;
        private String jobsPostalCode;

    private SortOfJobs sortOfJob;
    private int estimatedTime;
    private int numberOfChildren;
}
