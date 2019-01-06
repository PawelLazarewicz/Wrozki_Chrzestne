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

    @RequestMapping("/Employee/addEmployee")
    public String addEmployeeForm(Model model){
        model.addAttribute("employee", new EmployeeDto());
        return "addEmployeeHTML";
    }

    @RequestMapping(value = "/Employee/listEmployees", method = RequestMethod.POST)
    public String addEmployee(@ModelAttribute EmployeeDto employeeDto, Model model){
        Employee newEmployee = employeeBuilderService.entityFromDto(employeeDto);
        employeeRepository.save(newEmployee);

        allEmployees(model);

        return "redirect:/Employee/listEmployees";
    }

    @RequestMapping("/Employee/listEmployees")
    public String allEmployees(Model model){
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = employeeList.stream()
                .map(e -> employeeBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        model.addAttribute("employeesDtos", employeeDtos);

        return "employeesHTML";
    }

    @RequestMapping("Employee/show/{id}")
    public String getEmployee(@PathVariable Long id, Model model) {
        Employee selectedEmployee = employeeRepository.getOne(id);

        EmployeeDto selectedEmployeeDto = employeeBuilderService.DtoFromEntity(selectedEmployee);

        model.addAttribute("employee", selectedEmployeeDto);

        return "employeeHTML";
    }

//    @GetMapping("Employee/{id}/delete")
//    public ResponseEntity deleteEmployee(@PathVariable Long id) {
//        Employee selectedEmployee = employeeRepository.getOne(id);
//
//        EmployeeDto selectedEmployeeDto = employeeBuilderService.DtoFromEntity(selectedEmployee);
//
//        employeeRepository.delete(selectedEmployee);
//
//        return new ResponseEntity(selectedEmployeeDto, HttpStatus.OK);
//    }
//
//    @GetMapping("Employee/{id}/move")
//    public ResponseEntity moveEmployee(@PathVariable Long id) {
//        Employee selectedEmployee = employeeRepository.getOne(id);
//
//        EmployeeDto selectedEmployeeDto = employeeBuilderService.DtoFromEntity(selectedEmployee);
//
//        inactiveEmployees.add(selectedEmployee);
//        employeeRepository.delete(selectedEmployee);
//
//        return new ResponseEntity(selectedEmployeeDto, HttpStatus.OK);
//    }
//
//    @GetMapping("listEmployees/inactive")
//    public ResponseEntity allInactiveEmployees() {
//        List<Employee> inactiveEmployeeList = new ArrayList<>(inactiveEmployees);
//        List<EmployeeDto> inactiveEmployeeDtos = inactiveEmployeeList.stream()
//                .map(e -> employeeBuilderService.DtoFromEntity(e))
//                .collect(Collectors.toList());
//
//        return new ResponseEntity(inactiveEmployeeDtos, HttpStatus.OK);
//    }
}
