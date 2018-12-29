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

    @RequestMapping("/addEmployee")
    public String addEmployeeForm(Model model){
        model.addAttribute("employee", new EmployeeDto());
        return "addEmployeeHTML";
    }

    @RequestMapping(value = "employees", method = RequestMethod.POST)
    public String addEmployee(@ModelAttribute EmployeeDto employeeDto, Model model){
        Employee newEmployee = new Employee(null,employeeDto.getName(), employeeDto.getLastName(), employeeDto.getCity(), employeeDto.getAge(), employeeDto.getTelephoneNumber(), employeeDto.getMail());
        employeeRepository.save(newEmployee);

        allEmployees(model);

        return "redirect:listEmployees";
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
