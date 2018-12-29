package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    @PostMapping("addEmployee")
    public ResponseEntity addEmployee(@RequestBody EmployeeDto employeeDto){
        Employee newEmployee = new Employee(null,employeeDto.getName(), employeeDto.getLastName(), employeeDto.getCity(), employeeDto.getAge(), employeeDto.getTelephoneNumber(), employeeDto.getMail());
        employeeRepository.save(newEmployee);

        EmployeeDto newEmployeeDto = new EmployeeDto(newEmployee.getName(), newEmployee.getLastName(), newEmployee.getCity(), newEmployee.getAge(), newEmployee.getTelephoneNumber(), newEmployee.getMail());
        return new ResponseEntity(newEmployeeDto, HttpStatus.OK);
    }
}
