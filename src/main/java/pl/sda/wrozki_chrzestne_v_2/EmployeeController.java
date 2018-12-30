package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    @PostMapping("addEmployee")
    public ResponseEntity addEmployee(@RequestBody EmployeeDto employeeDto) {
        Employee newEmployee = new Employee(null, employeeDto.getName(), employeeDto.getLastName(), employeeDto.getCity(), employeeDto.getAge(), employeeDto.getTelephoneNumber(), employeeDto.getMail());
        employeeRepository.save(newEmployee);

        EmployeeDto newEmployeeDto = new EmployeeDto(newEmployee.getName(), newEmployee.getLastName(), newEmployee.getCity(), newEmployee.getAge(), newEmployee.getTelephoneNumber(), newEmployee.getMail());
        return new ResponseEntity(newEmployeeDto, HttpStatus.OK);
    }

    @GetMapping("listEmployees")
    public ResponseEntity allEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = employeeList.stream()
                .map(e -> new EmployeeDto(e.getName(), e.getLastName(), e.getCity(), e.getAge(), e.getTelephoneNumber(), e.getMail()))
                .collect(Collectors.toList());

        return new ResponseEntity(employeeDtos, HttpStatus.OK);
    }

    @GetMapping("Employee/{id}")
    public ResponseEntity getEmployee(@PathVariable Long id) {
        Employee selectedEmployee = employeeRepository.getOne(id);

        EmployeeDto selectedEmployeeDto = new EmployeeDto(selectedEmployee.getName(), selectedEmployee.getLastName(), selectedEmployee.getCity(), selectedEmployee.getAge(), selectedEmployee.getTelephoneNumber(), selectedEmployee.getMail());

        return new ResponseEntity(selectedEmployeeDto, HttpStatus.OK);
    }

    @GetMapping("Employee/{id}/delete")
    public ResponseEntity deleteEmployee(@PathVariable Long id){
        Employee selectedEmployee = employeeRepository.getOne(id);

        EmployeeDto selectedEmployeeDto = new EmployeeDto(selectedEmployee.getName(), selectedEmployee.getLastName(), selectedEmployee.getCity(), selectedEmployee.getAge(), selectedEmployee.getTelephoneNumber(), selectedEmployee.getMail());

        employeeRepository.delete(selectedEmployee);

        return new ResponseEntity(selectedEmployeeDto, HttpStatus.OK);
    }
}
