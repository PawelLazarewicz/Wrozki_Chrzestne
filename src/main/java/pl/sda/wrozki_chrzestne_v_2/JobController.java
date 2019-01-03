package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class JobController {

    private JobRepository jobRepository;

    private JobBuilderService jobBuilderService;
    private List<Job> inactiveJobs;

    @GetMapping("listJobs")
    public ResponseEntity allJobs() {
        List<Job> jobs = jobRepository.findAll();

        List<JobDto> jobDtos = jobs
                .stream()
                .map(e -> jobBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        return new ResponseEntity(jobDtos, HttpStatus.OK);
    }

    @PostMapping("addJob")
    public ResponseEntity addJob(@RequestBody JobDto jobDto){
        Job newJob = jobBuilderService.entityFromDto(jobDto);

        jobRepository.save(newJob);

        JobDto newJobDto = jobBuilderService.DtoFromEntity(newJob);

        return new ResponseEntity(newJobDto, HttpStatus.OK);
    }

    @GetMapping("Job/{id}")
    public ResponseEntity getJob(@PathVariable Long id){
        Job selectedJob = jobRepository.getOne(id);

        JobDto selectedJobDto = jobBuilderService.DtoFromEntity(selectedJob);

        return new ResponseEntity(selectedJobDto, HttpStatus.OK);
    }

    @GetMapping("Job/{id}/move")
    public ResponseEntity moveJob(@PathVariable Long id){
        Job selectedJob = jobRepository.getOne(id);
        JobDto selectedJobDto = jobBuilderService.DtoFromEntity(selectedJob);

        inactiveJobs.add(selectedJob);
        jobRepository.delete(selectedJob);

        return new ResponseEntity(selectedJobDto, HttpStatus.OK);
    }

}
