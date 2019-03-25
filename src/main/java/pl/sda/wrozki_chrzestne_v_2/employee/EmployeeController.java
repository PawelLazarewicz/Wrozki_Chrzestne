package pl.sda.wrozki_chrzestne_v_2.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;

import java.util.*;

@Controller
public class EmployeeController {

    private EmployeeFacade employeeFacade;

    @Autowired
    public void setEmployeeFacade(EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    private static final String LIST_EMPLOYEES = "redirect:/Employee/listEmployees";
    private EmployeeDto selectedEmployee;

    @RequestMapping("/Employee/addEmployee")
    public String addEmployeeForm(Model model) {
        model.addAttribute("newEmployee", new EmployeeDto());

        return "employee/addEmployeeHTML";
    }

    @RequestMapping(value = "/Employee/listEmployees", method = RequestMethod.POST)
    public String addEmployee(@ModelAttribute EmployeeDto employeeDto, Model model) {
        employeeFacade.addEmployee(employeeDto);
        allEmployees(model);

        return LIST_EMPLOYEES;
    }

    @RequestMapping("/Employee/listEmployees")
    public String allEmployees(Model model) {
        model.addAttribute("activeEmployeesDtos", employeeFacade.getActiveEmployeeList());

        model.addAttribute("inactiveEmployeesDtos", employeeFacade.getInactiveEmployeeList());

        model.addAttribute("countOfActiveJobsForActiveEmployee", employeeFacade.countOfActiveJobsForActiveEmployee());

        model.addAttribute("countOfCompletedJobsForActiveEmployee", employeeFacade.countOfCompletedJobsForActiveEmployee());

        model.addAttribute("countOfCompletedJobsForInactiveEmployee", employeeFacade.countOfCompletedJobsForInactiveEmployee());

        return "employee/employeesHTML";
    }

    @RequestMapping("Employee/{id}/move_Inactive")
    public String moveEmployeeInactive(@PathVariable Long id, Model model) {
        employeeFacade.moveEmployeeInactive(id);

        return LIST_EMPLOYEES;
    }

    @RequestMapping("Employee/{id}/show")
    public String getEmployee(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeFacade.getEmployee(id));

        return "employee/employeeHTML";
    }

    @RequestMapping("Employee/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        employeeFacade.deleteEmployee(id);

        return LIST_EMPLOYEES;
    }

    @RequestMapping("Employee/{id}/move_Active")
    public String moveEmployeeActive(@PathVariable Long id, Model model) {
        employeeFacade.moveEmployeeActive(id);

        return LIST_EMPLOYEES;
    }

    @RequestMapping("Employee/{id}/edit")
    public String editEmployee(@PathVariable Long id, Model model) {
        selectedEmployee = employeeFacade.editEmployee(id);
        model.addAttribute("employee", employeeFacade.editEmployee(id));

        return "employee/updateEmployeeHTML";
    }

    @RequestMapping(value = "/Employee/updateEmployee", method = RequestMethod.POST)
    public String updateEmployee(@ModelAttribute EmployeeDto employeeDto, Model model) {
        employeeFacade.updateEmployee(employeeDto, selectedEmployee);

        allEmployees(model);

        return LIST_EMPLOYEES;
    }

    public List<EmployeeDto> getActiveEmployeeList() {
        return employeeFacade.getActiveEmployeeList();
    }
}