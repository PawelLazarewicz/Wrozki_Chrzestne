package pl.sda.wrozki_chrzestne_v_2.employee;

import lombok.AllArgsConstructor;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;

@AllArgsConstructor
public class EmployeeFacade {

    private EmployeeRepository employeeRepository;
    private EmployeeBuilderService employeeBuilderService;


    public void addEmployee(EmployeeDto employeeDto) {
        Employee newEmployee = employeeBuilderService.entityFromDto(employeeDto);
        employeeRepository.save(newEmployee);
    }
}
