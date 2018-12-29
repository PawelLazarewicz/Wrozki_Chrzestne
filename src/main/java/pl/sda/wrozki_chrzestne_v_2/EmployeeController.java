package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
//@AllArgsConstructor
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("addEmployee")
    public ResponseEntity addEmployee(@RequestBody EmployeeDto employeeDto){
        Employee newEmployee = new Employee(null,employeeDto.getName(), employeeDto.getLastName(), employeeDto.getCity(), employeeDto.getAge(), employeeDto.getTelephoneNumber(), employeeDto.getMail());
        employeeRepository.save(newEmployee);

        EmployeeDto newEmployeeDto = new EmployeeDto(newEmployee.getName(), newEmployee.getLastName(), newEmployee.getCity(), newEmployee.getAge(), newEmployee.getTelephoneNumber(), newEmployee.getMail());
        return new ResponseEntity(newEmployeeDto, HttpStatus.OK);
    }

    @RequestMapping("/listEmployees")
    public String allEmployees(Model model){
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = employeeList.stream()
                .map(e -> new EmployeeDto(e.getName(), e.getLastName(), e.getCity(), e.getAge(), e.getTelephoneNumber(), e.getMail()))
                .collect(Collectors.toList());

        model.addAttribute("employeesDtos", employeeDtos);

        return "employeesHTML";
    }
}
