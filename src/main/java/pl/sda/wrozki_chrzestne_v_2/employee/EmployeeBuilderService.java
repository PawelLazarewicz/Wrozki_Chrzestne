package pl.sda.wrozki_chrzestne_v_2.employee;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;
import pl.sda.wrozki_chrzestne_v_2.job.Job;
import pl.sda.wrozki_chrzestne_v_2.job.JobBuilderService;
import pl.sda.wrozki_chrzestne_v_2.job.JobController;
import pl.sda.wrozki_chrzestne_v_2.job.JobStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeBuilderService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JobBuilderService jobBuilderService;

    @Autowired
    private JobController jobController;

    public Employee entityFromDto(EmployeeDto employeeDto) {
        Employee employee = new Employee();

        employee.setId(null);
        coreEntityFromDto(employee, employeeDto);
//        employee.setName(employeeDto.getName());
//        employee.setLastName(employeeDto.getLastName());
//        employee.setCity(employeeDto.getCity());
//        employee.setAge(employeeDto.getAge());
//        employee.setTelephoneNumber(employeeDto.getTelephoneNumber());
//        employee.setMail(employeeDto.getMail());
//        employee.setEmployeeStatus(employeeDto.getEmployeeStatus());
//        employee.setAssignedForJobs(employeeDto.isAssignedForJobs());
        List<Job> jobs = employeeDto.getWorkedJobs()
                .stream()
                .map(e -> jobBuilderService.entityFromDto(e))
                .collect(Collectors.toList());
        employee.setWorkedJobs(jobs);

        return employee;
    }

    public EmployeeDto dtoFromEntity(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId(employee.getId());
        employeeDto.setName(employee.getName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setCity(employee.getCity());
        employeeDto.setAge(employee.getAge());
        employeeDto.setTelephoneNumber(employee.getTelephoneNumber());
        employeeDto.setMail(employee.getMail());
        employeeDto.setEmployeeStatus(employee.getEmployeeStatus());

        employeeDto.setAssignedForJobs(employee.isAssignedForJobs());


        return employeeDto;
    }

    public EmployeeDto dtoFromEntityWithJobs(Employee employee) {
        EmployeeDto employeeDto = dtoFromEntity(employee);
//        EmployeeDto employeeDto = new EmployeeDto();
//
//        employeeDto.setId(employee.getId());
//        employeeDto.setName(employee.getName());
//        employeeDto.setLastName(employee.getLastName());
//        employeeDto.setCity(employee.getCity());
//        employeeDto.setAge(employee.getAge());
//        employeeDto.setTelephoneNumber(employee.getTelephoneNumber());
//        employeeDto.setMail(employee.getMail());
//        employeeDto.setEmployeeStatus(employee.getEmployeeStatus());

        List<JobDto> activeJobs = employee.getWorkedJobs()
                .stream()
                .filter(job -> job.getJobStatus().equals(JobStatus.ACTIVE))
                .map(e -> jobBuilderService.dtoFromEntity(e))
                .collect(Collectors.toList());

        List<JobDto> completedJobs = jobController.getCompletedJobList()
                .stream()
                .filter(jobDto -> jobDto.getEmployees()
                        .stream()
                        .anyMatch(employeeDto1 -> employeeDto1.getId().equals(employeeDto.getId())))
                .collect(Collectors.toList());

        List<JobDto> allJobs = new ArrayList<>();
        allJobs.addAll(activeJobs);
        allJobs.addAll(completedJobs);

        employeeDto.setWorkedJobs(allJobs);
//        employeeDto.setAssignedForJobs(employee.isAssignedForJobs());

        return employeeDto;
    }

    public Employee selectEmployee(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        Employee employee = optionalEmployee.get();

        return employee;
    }

    public Employee updateEntityFromDto(EmployeeDto employeeDto, Employee employee) {

        employee.setId(employee.getId());
        coreEntityFromDto(employee, employeeDto);
//        employee.setName(employeeDto.getName());
//        employee.setLastName(employeeDto.getLastName());
//        employee.setCity(employeeDto.getCity());
//        employee.setAge(employeeDto.getAge());
//        employee.setTelephoneNumber(employeeDto.getTelephoneNumber());
//        employee.setMail(employeeDto.getMail());
//        employeeDto.setEmployeeStatus(employee.getEmployeeStatus());

//        employee.setAssignedForJobs(employeeDto.isAssignedForJobs());

        return employee;
    }

    public Employee coreEntityFromDto(Employee employee, EmployeeDto employeeDto) {
        employee.setName(employeeDto.getName());
        employee.setLastName(employeeDto.getLastName());
        employee.setCity(employeeDto.getCity());
        employee.setAge(employeeDto.getAge());
        employee.setTelephoneNumber(employeeDto.getTelephoneNumber());
        employee.setMail(employeeDto.getMail());
        employeeDto.setEmployeeStatus(employee.getEmployeeStatus());

        employee.setAssignedForJobs(employeeDto.isAssignedForJobs());

        return employee;
    }
}