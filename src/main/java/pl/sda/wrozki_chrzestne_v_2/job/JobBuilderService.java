package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;

import java.util.Optional;

public class JobBuilderService {

    @Autowired
    private JobRepository jobRepository;

    public Job entityFromDto(JobDto jobDto) {
        Job job = new Job();

        job.setId(null);
        job.setClientName(jobDto.getClientName());
        job.setClientLastName(jobDto.getClientLastName());
        job.setDateOfJob(jobDto.getDateOfJob());
        job.setCity(jobDto.getCity());
        job.setJobsAddress(jobDto.getJobsAddress());
        job.setJobsPostalCode(jobDto.getJobsPostalCode());
        job.setSortOfJob(jobDto.getSortOfJob());
        job.setEstimatedTime(jobDto.getEstimatedTime());
        job.setNumberOfChildren(jobDto.getNumberOfChildren());

        return job;
    }

    public JobDto DtoFromEntity(Job job) {
        JobDto jobDto = new JobDto();

        jobDto.setClientName(job.getClientName());
        jobDto.setClientLastName(job.getClientLastName());
        jobDto.setDateOfJob(job.getDateOfJob());
        jobDto.setCity(job.getCity());
        jobDto.setJobsAddress(job.getJobsAddress());
        jobDto.setJobsPostalCode(job.getJobsPostalCode());
        jobDto.setSortOfJob(job.getSortOfJob());
        jobDto.setEstimatedTime(job.getEstimatedTime());
        jobDto.setNumberOfChildren(job.getNumberOfChildren());

        return jobDto;
    }

    public Job selectJob(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        Job job = optionalJob.get();

        return job;
    }
}
