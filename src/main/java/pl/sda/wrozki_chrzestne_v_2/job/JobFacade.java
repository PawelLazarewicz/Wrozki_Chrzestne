package pl.sda.wrozki_chrzestne_v_2.job;

import lombok.AllArgsConstructor;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JobFacade {

    private JobRepository jobRepository;
    private JobBuilderService jobBuilderService;

    public void addJob(JobDto jobDto) {
        Job newJob = jobBuilderService.entityFromDto(jobDto);
        jobRepository.save(newJob);
    }

    public List<JobDto> getUncompletedJobList() {

        return jobRepository.findAll()
                .stream()
                .filter(job -> job.getJobStatus().equals(JobStatus.ACTIVE))
                .map(e -> jobBuilderService.dtoFromEntityWithEmployees(e))
                .collect(Collectors.toList());
    }
}
