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

    public void moveJobCompleted(Long id) {
        Job jobToMove = jobBuilderService.selectJob(id);
        JobDto selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(jobToMove);
        selectedJobDto.setJobStatus(JobStatus.COMPLETED);

        // lambda for set employee assigned for job as FALSE
        selectedJobDto.getEmployees()
                .stream()
                .filter(employee -> employee.getWorkedJobs()
                        .stream()
                        .allMatch(job -> job.getJobStatus().equals(JobStatus.COMPLETED)))
                .peek(employee -> employee.setAssignedForJobs(false))
                .collect(Collectors.toList());

        jobToMove = jobBuilderService.updateEntityFromDto(selectedJobDto, jobToMove);
        jobRepository.save(jobToMove);

        JobDto jobToMoveCompleted = jobBuilderService.dtoFromEntityWithEmployees(jobToMove);

        if (getCompletedJobList().isEmpty()) {
            getCompletedJobList().add(jobToMoveCompleted);
        } else {
            for (JobDto completedJob : getCompletedJobList()) {
                if (completedJob.getId().equals(jobToMoveCompleted.getId())) {
                    break;
                }
            }
            getCompletedJobList().add(jobToMoveCompleted);
        }
    }
}
