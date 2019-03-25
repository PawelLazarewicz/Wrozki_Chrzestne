package pl.sda.wrozki_chrzestne_v_2.job;

import lombok.AllArgsConstructor;
import pl.sda.wrozki_chrzestne_v_2.client.Client;
import pl.sda.wrozki_chrzestne_v_2.client.ClientBuilderService;
import pl.sda.wrozki_chrzestne_v_2.client.ClientController;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;
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
    private ClientController clientController;
    private ClientBuilderService clientBuilderService;

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
        //Job finalSelectedJob = job;

        Optional<JobDto> completedJobToShow = getCompletedJobList()
                .stream()
                .filter(jobDto -> jobDto.getId().equals(job.getId()))
                .findFirst();

        JobDto selectedJobDto;
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

        employeeAssignedForJobAsFalse(selectedJobDto);

        jobToMove = jobBuilderService.updateEntityFromDto(selectedJobDto, jobToMove);
        jobRepository.save(jobToMove);

        moveJobToCompletedJobList(jobToMove);
    }

    private void moveJobToCompletedJobList(Job jobToMove) {
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

    private List<EmployeeDto> employeeAssignedForJobAsFalse(JobDto selectedJobDto) {
        // lambda for set employee assigned for job as FALSE
        return selectedJobDto.getEmployees()
                .stream()
                .filter(employee -> employee.getWorkedJobs()
                        .stream()
                        .allMatch(job -> job.getJobStatus().equals(JobStatus.COMPLETED)))
                .peek(employee -> employee.setAssignedForJobs(false))
                .collect(Collectors.toList());
    }

    public List<ClientDto> getClientsToChange(JobDto selectedJobDto) {
        List<ClientDto> clientsToChange = clientController.getAllClients();
        ClientDto selectedClientDto = selectedJobDto.getClient();

        for (ClientDto clientDto : clientsToChange) {
            if (clientDto.getId().equals(selectedClientDto.getId())) {
                clientsToChange.remove(clientDto);
                break;
            }
        }
        return clientsToChange;
    }

    public void updateJob(JobDto updatingJobDto, JobDto selectingJobDto) {
        Job job = jobBuilderService.selectJob(selectingJobDto.getId());
        updatingJobDto.setClient(selectingJobDto.getClient());
        job = jobBuilderService.updateEntityFromDto(updatingJobDto, job);
        jobRepository.save(job);
    }

    public Long selectClient(ClientDto clientDto, JobDto jobDto) {
        Client client = clientBuilderService.selectClient(clientDto.getId());
        ClientDto selectedClientDto = clientBuilderService.dtoFromEntity(client);
        jobDto.setClient(selectedClientDto);

        Job newJob = jobBuilderService.entityFromDtoWithUpdatingClient(jobDto, selectedClientDto);
        jobRepository.save(newJob);

        return newJob.getId();
    }

    public void changeSelectedClient(Long idJob, ClientDto clientDto) {
        Client client = clientBuilderService.selectClient(clientDto.getId());
        ClientDto selectedClientDto = clientBuilderService.dtoFromEntity(client);

        Job job = jobBuilderService.selectJob(idJob);
        JobDto editedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);
        editedJobDto.setClient(selectedClientDto);

        job = jobBuilderService.updateEntityFromDto(editedJobDto, job);
        jobRepository.save(job);
    }
}
