package pl.sda.wrozki_chrzestne_v_2.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeBuilderService employeeBuilderService;

    private List<Employee> inactiveEmployeeList = new ArrayList<>();

    @RequestMapping("/Employee/addEmployee")
    public String addEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeDto());
        return "addEmployeeHTML";
    }

    @RequestMapping(value = "/Employee/listEmployees", method = RequestMethod.POST)
    public String addEmployee(@ModelAttribute EmployeeDto employeeDto, Model model) {
        Employee newEmployee = employeeBuilderService.entityFromDto(employeeDto);
        employeeRepository.save(newEmployee);

        allEmployees(model);

        return "redirect:/Employee/listEmployees";
    }

    @RequestMapping("/Employee/listEmployees")
    public String allEmployees(Model model) {
        List<Employee> employeeList = employeeRepository.findAll();

        for (int i = 0; i < employeeList.size(); i++) {
            for (int j = 0; j < inactiveEmployeeList.size(); j++) {
                if ((employeeList.get(i).getId()).equals(inactiveEmployeeList.get(j).getId())) {
                    employeeList.remove(employeeList.get(i));
                }
            }
        }

        List<EmployeeDto> employeeDtos = employeeList.stream()
                .map(e -> employeeBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        model.addAttribute("employeesDtos", employeeDtos);

        return "employeesHTML";
    }

    @RequestMapping("Employee/{id}/move")
    public String moveEmployee(@PathVariable Long id, Model model) {
        Optional<Employee> selectedEmployeeOptional = employeeRepository.findById(id);
        Employee selectedEmployee = selectedEmployeeOptional.get();
        inactiveEmployeeList.add(selectedEmployee);

        EmployeeDto selectedEmployeeDto = employeeBuilderService.DtoFromEntity(selectedEmployee);

        model.addAttribute("employee", selectedEmployeeDto);

        return "employeeHTML";
    }

    @RequestMapping("Employee/{id}/show")
    public String getEmployee(@PathVariable Long id, Model model) {
        Employee selectedEmployee = employeeRepository.getOne(id);

        EmployeeDto selectedEmployeeDto = employeeBuilderService.DtoFromEntity(selectedEmployee);

        model.addAttribute("employee", selectedEmployeeDto);

        return "employeeHTML";
    }

    @RequestMapping("Employee/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        Employee selectedEmployee = employeeRepository.getOne(id);

        EmployeeDto selectedEmployeeDto = employeeBuilderService.DtoFromEntity(selectedEmployee);

        model.addAttribute("employee", selectedEmployeeDto);

        employeeRepository.delete(selectedEmployee);

        return "employeeHTML";
    }

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
