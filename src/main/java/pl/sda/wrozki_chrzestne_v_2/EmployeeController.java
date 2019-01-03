package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    private List<Employee> inactiveEmployees;

    private EmployeeBuilderService employeeBuilderService;

    @PostMapping("addEmployee")
    public ResponseEntity addEmployee(@RequestBody EmployeeDto employeeDto) {
        Employee newEmployee = employeeBuilderService.entityFromDto(employeeDto);
        employeeRepository.save(newEmployee);
        EmployeeDto newEmployeeDto = employeeBuilderService.DtoFromEntity(newEmployee);

        return new ResponseEntity(newEmployeeDto, HttpStatus.OK);
    }

    @GetMapping("listEmployees")
    public ResponseEntity allEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = employeeList.stream()
                .map(e -> employeeBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        return new ResponseEntity(employeeDtos, HttpStatus.OK);
    }

    @GetMapping("Employee/{id}")
    public ResponseEntity getEmployee(@PathVariable Long id) {
        Employee selectedEmployee = employeeRepository.getOne(id);

        EmployeeDto selectedEmployeeDto = employeeBuilderService.DtoFromEntity(selectedEmployee);

        return new ResponseEntity(selectedEmployeeDto, HttpStatus.OK);
    }

    @GetMapping("Employee/{id}/delete")
    public ResponseEntity deleteEmployee(@PathVariable Long id) {
        Employee selectedEmployee = employeeRepository.getOne(id);

        EmployeeDto selectedEmployeeDto = employeeBuilderService.DtoFromEntity(selectedEmployee);

        employeeRepository.delete(selectedEmployee);

        return new ResponseEntity(selectedEmployeeDto, HttpStatus.OK);
    }

    @GetMapping("Employee/{id}/move")
    public ResponseEntity moveEmployee(@PathVariable Long id) {
        Employee selectedEmployee = employeeRepository.getOne(id);

        EmployeeDto selectedEmployeeDto = employeeBuilderService.DtoFromEntity(selectedEmployee);

        inactiveEmployees.add(selectedEmployee);
        employeeRepository.delete(selectedEmployee);

        return new ResponseEntity(selectedEmployeeDto, HttpStatus.OK);
    }

    @GetMapping("listEmployees/inactive")
    public ResponseEntity allInactiveEmployees() {
        List<Employee> inactiveEmployeeList = new ArrayList<>(inactiveEmployees);
        List<EmployeeDto> inactiveEmployeeDtos = inactiveEmployeeList.stream()
                .map(e -> employeeBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        return new ResponseEntity(inactiveEmployeeDtos, HttpStatus.OK);
    }
}
