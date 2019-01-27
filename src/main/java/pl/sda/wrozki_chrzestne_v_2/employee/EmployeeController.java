package pl.sda.wrozki_chrzestne_v_2.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeBuilderService employeeBuilderService;

    private List<Employee> inactiveEmployeeList = new ArrayList<>();
    private List<Employee> activeEmployeeList = new ArrayList<>();
    private Employee editedEmployee;

    @RequestMapping("/Employee/addEmployee")
    public String addEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeDto());
        return "employee/addEmployeeHTML";
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
        List<EmployeeDto> employeeDtos = getActiveEmployeeList().stream()
                .map(e -> employeeBuilderService.dtoFromEntityWithJobs(e))
                .collect(Collectors.toList());

        model.addAttribute("employeesDtos", employeeDtos);

        List<EmployeeDto> inactiveEmployeeDtos = inactiveEmployeeList.stream()
                .map(e -> employeeBuilderService.dtoFromEntityWithJobs(e))
                .collect(Collectors.toList());

        model.addAttribute("inactiveEmployeesDtos", inactiveEmployeeDtos);

        return "employee/employeesHTML";
    }

    @RequestMapping("Employee/{id}/move_Inactive")
    public String moveEmployeeInactive(@PathVariable Long id, Model model) {
        Employee selectedEmployee = employeeBuilderService.selectEmployee(id);

        if (inactiveEmployeeList.isEmpty()) {
            inactiveEmployeeList.add(selectedEmployee);
        } else {
            for (Employee inactiveEmployee : inactiveEmployeeList) {
                if (!inactiveEmployee.getId().equals(selectedEmployee.getId())) {
                    inactiveEmployeeList.add(selectedEmployee);
                }
            }
        }

        EmployeeDto selectedEmployeeDto = employeeBuilderService.dtoFromEntityWithJobs(selectedEmployee);

        model.addAttribute("employee", selectedEmployeeDto);

        return "redirect:/Employee/listEmployees";
    }

    @RequestMapping("Employee/{id}/show")
    public String getEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeBuilderService.selectEmployee(id);
        EmployeeDto employeeDto = employeeBuilderService.dtoFromEntityWithJobs(employee);

        model.addAttribute("employee", employeeDto);

        return "employee/employeeHTML";
    }

    @RequestMapping("Employee/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeBuilderService.selectEmployee(id);
        EmployeeDto employeeDto = employeeBuilderService.dtoFromEntityWithJobs(employee);

        for (Employee inactiveEmployee : inactiveEmployeeList) {
            if (inactiveEmployee.getId().equals(employee.getId())) {
                inactiveEmployeeList.remove(inactiveEmployee);
            }
            break;
        }

        model.addAttribute("employee", employeeDto);

        employeeRepository.delete(employee);

        return "redirect:/Employee/listEmployees";
    }

    @RequestMapping("Employee/{id}/move_Active")
    public String moveEmployeeActive(@PathVariable Long id, Model model) {
        Employee selectedEmployee = employeeBuilderService.selectEmployee(id);

        for (Employee inactiveEmployee : inactiveEmployeeList) {
            if (inactiveEmployee.getId().equals(selectedEmployee.getId())) {
                inactiveEmployeeList.remove(inactiveEmployee);
            }
            break;
        }

        EmployeeDto selectedEmployeeDto = employeeBuilderService.dtoFromEntityWithJobs(selectedEmployee);

        model.addAttribute("employee", selectedEmployeeDto);

        return "redirect:/Employee/listEmployees";
    }

    @RequestMapping("Employee/{id}/edit")
    public String editEmployee(@PathVariable Long id, Model model){
        editedEmployee = employeeBuilderService.selectEmployee(id);
        EmployeeDto editedEmployeeDto = employeeBuilderService.dtoFromEntityWithJobs(editedEmployee);

        model.addAttribute("editedEmployee", editedEmployeeDto);

        return "employee/updateEmployeeHTML";
    }

    @RequestMapping(value = "/Employee/updateEmployee", method = RequestMethod.POST)
    public String updateEmployee(@ModelAttribute EmployeeDto employeeDto, Model model) {
        editedEmployee = employeeBuilderService.updateEntityFromDto(employeeDto, editedEmployee);
        employeeRepository.save(editedEmployee);

        allEmployees(model);

        return "redirect:/Employee/listEmployees";
    }

    public List<Employee> getActiveEmployeeList() {
        activeEmployeeList = employeeRepository.findAll();

        for (Employee activeEmployee : activeEmployeeList) {
            for (Employee inactiveEmployee : inactiveEmployeeList) {
                if ((activeEmployee.getId()).equals(inactiveEmployee.getId())) {
                    activeEmployeeList.remove(activeEmployee);
                }
            }
        }
        return activeEmployeeList;
    }
}
