package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobDto {

    private String clientName;
    private String clientLastName;
    private Date dateOfJob;
    private String jobsPlace;

        private String jobsAddress;
        private String jobsPostalCode;

    private SortOfJobs sortOfJob;
    private int estimatedTime;
    private int numberOfChildren;
}
