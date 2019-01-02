package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class JobController {

    private JobRepository jobRepository;

    @GetMapping("/listJobs")
    public ResponseEntity allJobs(){
        List<Job> jobs = jobRepository.findAll();

        List<JobDto> jobDtos = jobs
                .stream()
                .map(e -> new JobDto(e.getClientName(), e.getClientLastName(), e.getDateOfJob(), e.getCity(), e.getJobsAddress(), e.getJobsPostalCode(), e.getSortOfJob(), e.getEstimatedTime(), e.getNumberOfChildren()))
                .collect(Collectors.toList());

        return new ResponseEntity(jobDtos, HttpStatus.OK);
     }
}
