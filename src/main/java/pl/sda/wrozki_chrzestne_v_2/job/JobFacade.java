package pl.sda.wrozki_chrzestne_v_2.job;

import lombok.AllArgsConstructor;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;

@AllArgsConstructor
public class JobFacade {

    private JobRepository jobRepository;
    private JobBuilderService jobBuilderService;

    public void addJob(JobDto jobDto) {
        Job newJob = jobBuilderService.entityFromDto(jobDto);
        jobRepository.save(newJob);
    }
}
