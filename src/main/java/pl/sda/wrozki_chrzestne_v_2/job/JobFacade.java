package pl.sda.wrozki_chrzestne_v_2.job;

import lombok.AllArgsConstructor;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JobFacade {

    private JobRepository jobRepository;
    private JobBuilderService jobBuilderService;
    private JobController jobController;
    private EmployeeController employeeController;

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

    public List<JobDto> getCompletedJobList() {
        return jobController.getCompletedJobsField();
    }

    public JobDto getJob(Long id) {
        Job job = jobBuilderService.selectJob(id);
        JobDto selectedJobDto = jobBuilderService.dtoFromEntity(job);

        JobDto finalSelectedJobDto = selectedJobDto;
        Optional<JobDto> completedJobToShow = getCompletedJobList()
                .stream()
                .filter(jobDto -> jobDto.getId().equals(finalSelectedJobDto.getId()))
                .findFirst();

        if (completedJobToShow.isPresent()) {
            selectedJobDto = completedJobToShow.get();
        } else {
            selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);
        }
        return selectedJobDto;
    }

    public List<EmployeeDto> getActiveEmployeeList() {

        return employeeController.getActiveEmployeeList();
    }
}
