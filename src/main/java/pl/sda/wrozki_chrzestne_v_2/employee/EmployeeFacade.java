package pl.sda.wrozki_chrzestne_v_2.employee;

import lombok.AllArgsConstructor;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;
import pl.sda.wrozki_chrzestne_v_2.job.JobStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class EmployeeFacade {

    private EmployeeRepository employeeRepository;
    private EmployeeBuilderService employeeBuilderService;


    public void addEmployee(EmployeeDto employeeDto) {
        Employee newEmployee = employeeBuilderService.entityFromDto(employeeDto);
        employeeRepository.save(newEmployee);
    }

    public List<EmployeeDto> getActiveEmployeeList() {
        List<EmployeeDto> activeEmployeeList = employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getEmployeeStatus().equals(EmployeeStatus.ACTIVE))
                .map(e -> employeeBuilderService.dtoFromEntityWithJobs(e))
                .collect(Collectors.toList());

        return activeEmployeeList;
    }

    public List<EmployeeDto> getInactiveEmployeeList() {
        List<EmployeeDto> inactiveEmployeeList = employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getEmployeeStatus().equals(EmployeeStatus.INACTIVE))
                .map(e -> employeeBuilderService.dtoFromEntityWithJobs(e))
                .collect(Collectors.toList());

        return inactiveEmployeeList;
    }

    public Map<Long, Long> countOfActiveJobsForActiveEmployee() {

        //LAMBDA for displaying counter for active employee active jobs
        Map<Long, Long> countOfActiveJobsForActiveEmployee = new HashMap<>();

        getActiveEmployeeList().forEach(employeeDto -> countOfActiveJobsForActiveEmployee
                .put(employeeDto.getId(), employeeDto.getWorkedJobs()
                        .stream()
                        .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.ACTIVE))
                        .count()));

        return countOfActiveJobsForActiveEmployee;
    }

    public Map<Long, Long> countOfCompletedJobsForActiveEmployee() {

        //LAMBDA for displaying counter for active employee completed jobs
        Map<Long, Long> countOfCompletedJobsForActiveEmployee = new HashMap<>();

        getActiveEmployeeList().forEach(employeeDto -> countOfCompletedJobsForActiveEmployee
                .put(employeeDto.getId(), employeeDto.getWorkedJobs()
                        .stream()
                        .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.COMPLETED))
                        .count()));

        return countOfCompletedJobsForActiveEmployee;
    }

    public Map<Long, Long> countOfCompletedJobsForInactiveEmployee() {

        //LAMBDA for displaying counter for inactive employee completed jobs
        Map<Long, Long> countOfCompletedJobsForInactiveEmployee = new HashMap<>();

        getInactiveEmployeeList().forEach(employeeDto -> countOfCompletedJobsForInactiveEmployee
                .put(employeeDto.getId(), employeeDto.getWorkedJobs()
                        .stream()
                        .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.COMPLETED))
                        .count()));

        return countOfCompletedJobsForInactiveEmployee;
    }
}