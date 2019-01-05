package pl.sda.wrozki_chrzestne_v_2.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeBuilderService employeeBuilderService;

    @RequestMapping("/addEmployee")
    public String addEmployeeForm(Model model){
        model.addAttribute("employee", new EmployeeDto());
        return "addEmployeeHTML";
    }

    @RequestMapping(value = "employees", method = RequestMethod.POST)
    public String addEmployee(@ModelAttribute EmployeeDto employeeDto, Model model){
        Employee newEmployee = employeeBuilderService.entityFromDto(employeeDto);
        employeeRepository.save(newEmployee);

        allEmployees(model);

        return "redirect:listEmployees";
    }

    @RequestMapping("/listEmployees")
    public String allEmployees(Model model){
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = employeeList.stream()
                .map(e -> employeeBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        model.addAttribute("employeesDtos", employeeDtos);

        return "employeesHTML";
    }
}
