package pl.sda.wrozki_chrzestne_v_2.employee;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;
import pl.sda.wrozki_chrzestne_v_2.job.Job;
import pl.sda.wrozki_chrzestne_v_2.job.JobBuilderService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeBuilderService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JobBuilderService jobBuilderService;

    @Autowired
    private EmployeeController employeeController;

    public Employee entityFromDto(EmployeeDto employeeDto) {
        Employee employee = new Employee();

        employee.setId(null);
        employee.setName(employeeDto.getName());
        employee.setLastName(employeeDto.getLastName());
        employee.setCity(employeeDto.getCity());
        employee.setAge(employeeDto.getAge());
        employee.setTelephoneNumber(employeeDto.getTelephoneNumber());
        employee.setMail(employeeDto.getMail());

        List<Job> jobs = employeeDto.getWorkedJobs()
                .stream()
                .map(e -> jobBuilderService.entityFromDto(e))
                .collect(Collectors.toList());

        employee.setWorkedJobs(jobs);

        return employee;
    }

    public EmployeeDto dtoFromEntity(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setName(employee.getName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setCity(employee.getCity());
        employeeDto.setAge(employee.getAge());
        employeeDto.setTelephoneNumber(employee.getTelephoneNumber());
        employeeDto.setMail(employee.getMail());

        return employeeDto;
    }

    public EmployeeDto dtoFromEntityWithJobs(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setName(employee.getName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setCity(employee.getCity());
        employeeDto.setAge(employee.getAge());
        employeeDto.setTelephoneNumber(employee.getTelephoneNumber());
        employeeDto.setMail(employee.getMail());

        List<JobDto> jobs = employee.getWorkedJobs()
                .stream()
                .map(e -> jobBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        employeeDto.setWorkedJobs(jobs);

        return employeeDto;
    }

    public Employee selectEmployee(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        Employee employee = optionalEmployee.get();

        return employee;
    }

    public Employee updateEntityFromDto(EmployeeDto employeeDto, Employee employee) {

        employee.setId(employee.getId());
        employee.setName(employeeDto.getName());
        employee.setLastName(employeeDto.getLastName());
        employee.setCity(employeeDto.getCity());
        employee.setAge(employeeDto.getAge());
        employee.setTelephoneNumber(employeeDto.getTelephoneNumber());
        employee.setMail(employeeDto.getMail());

        return employee;
    }

    public List<Employee> getActiveEmployeeList() {
        return employeeController.activeEmployeeList;
    }
}
