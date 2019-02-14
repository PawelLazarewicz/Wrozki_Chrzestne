package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sda.wrozki_chrzestne_v_2.client.Client;
import pl.sda.wrozki_chrzestne_v_2.client.ClientBuilderService;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;
import pl.sda.wrozki_chrzestne_v_2.employee.Employee;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeBuilderService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JobBuilderService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployeeBuilderService employeeBuilderService;

    @Autowired
    private ClientBuilderService clientBuilderService;

    public Job entityFromDto(JobDto jobDto) {
        Job job = new Job();

        job.setId(null);

        Client client = clientBuilderService.entityFromDto(jobDto.getClient());
        job.setClient(client);

//        job.setClientName(jobDto.getClientName());
//        job.setClientLastName(jobDto.getClientLastName());
        job.setDateOfJob(jobDto.getDateOfJob());
        job.setCity(jobDto.getCity());
        job.setJobsAddress(jobDto.getJobsAddress());
        job.setJobsPostalCode(jobDto.getJobsPostalCode());
        job.setSortOfJob(jobDto.getSortOfJob());
        job.setEstimatedTime(jobDto.getEstimatedTime());
        job.setNumberOfChildren(jobDto.getNumberOfChildren());

        List<Employee> employees = jobDto.getEmployees()
                .stream()
                .map(e->employeeBuilderService.entityFromDto(e))
                .collect(Collectors.toList());

        job.setEmployees(employees);

        return job;
    }

    public JobDto dtoFromEntity(Job job) {
        JobDto jobDto = new JobDto();

        jobDto.setId(job.getId());

        ClientDto clientDto = clientBuilderService.dtoFromEntity(job.getClient());
        jobDto.setClient(clientDto);

//        jobDto.setClientName(job.getClientName());
//        jobDto.setClientLastName(job.getClientLastName());
        jobDto.setDateOfJob(job.getDateOfJob());
        jobDto.setCity(job.getCity());
        jobDto.setJobsAddress(job.getJobsAddress());
        jobDto.setJobsPostalCode(job.getJobsPostalCode());
        jobDto.setSortOfJob(job.getSortOfJob());
        jobDto.setEstimatedTime(job.getEstimatedTime());
        jobDto.setNumberOfChildren(job.getNumberOfChildren());

        return jobDto;
    }

    public JobDto dtoFromEntityWithEmployees(Job job) {
        JobDto jobDto = new JobDto();

        jobDto.setId(job.getId());

        ClientDto clientDto = clientBuilderService.dtoFromEntity(job.getClient());
        jobDto.setClient(clientDto);

//        jobDto.setClientName(job.getClientName());
//        jobDto.setClientLastName(job.getClientLastName());
        jobDto.setDateOfJob(job.getDateOfJob());
        jobDto.setCity(job.getCity());
        jobDto.setJobsAddress(job.getJobsAddress());
        jobDto.setJobsPostalCode(job.getJobsPostalCode());
        jobDto.setSortOfJob(job.getSortOfJob());
        jobDto.setEstimatedTime(job.getEstimatedTime());
        jobDto.setNumberOfChildren(job.getNumberOfChildren());

        List<EmployeeDto> employeesDto = job.getEmployees()
                .stream()
                .map(e -> employeeBuilderService.dtoFromEntity(e))
                .collect(Collectors.toList());

        jobDto.setEmployees(employeesDto);

        return jobDto;
    }

    public Job selectJob(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        Job job = optionalJob.get();

        return job;
    }

    public Job updateEntityFromDto(JobDto jobDto, Job job) {

        job.setId(job.getId());
        job.setClient(job.getClient());
//        job.setClientName(jobDto.getClientName());
//        job.setClientLastName(jobDto.getClientLastName());
        job.setDateOfJob(jobDto.getDateOfJob());
        job.setCity(jobDto.getCity());
        job.setJobsAddress(jobDto.getJobsAddress());
        job.setJobsPostalCode(jobDto.getJobsPostalCode());
        job.setSortOfJob(jobDto.getSortOfJob());
        job.setEstimatedTime(jobDto.getEstimatedTime());
        job.setNumberOfChildren(jobDto.getNumberOfChildren());

        return job;
    }

    public Job entityFromDtoWithUpdatingClient(JobDto jobDto, ClientDto clientDto) {
        Job job = new Job();

        job.setId(null);

        Client client = clientBuilderService.selectClientFromDto(clientDto);
        job.setClient(client);

//        job.setClientName(jobDto.getClientName());
//        job.setClientLastName(jobDto.getClientLastName());
        job.setDateOfJob(jobDto.getDateOfJob());
        job.setCity(jobDto.getCity());
        job.setJobsAddress(jobDto.getJobsAddress());
        job.setJobsPostalCode(jobDto.getJobsPostalCode());
        job.setSortOfJob(jobDto.getSortOfJob());
        job.setEstimatedTime(jobDto.getEstimatedTime());
        job.setNumberOfChildren(jobDto.getNumberOfChildren());

        List<Employee> employees = jobDto.getEmployees()
                .stream()
                .map(e->employeeBuilderService.entityFromDto(e))
                .collect(Collectors.toList());

        job.setEmployees(employees);

        return job;
    }
}
