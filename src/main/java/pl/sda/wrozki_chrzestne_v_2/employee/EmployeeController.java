package pl.sda.wrozki_chrzestne_v_2.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;
import pl.sda.wrozki_chrzestne_v_2.job.Job;
import pl.sda.wrozki_chrzestne_v_2.job.JobStatus;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    private EmployeeBuilderService employeeBuilderService;

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    public void setEmployeeBuilderService(EmployeeBuilderService employeeBuilderService) {
        this.employeeBuilderService = employeeBuilderService;
    }

    private static final String LIST_EMPLOYEES = "redirect:/Employee/listEmployees";

    private List<EmployeeDto> inactiveEmployeeList = new ArrayList<>();
    private List<EmployeeDto> activeEmployeeList = new ArrayList<>();
    private EmployeeDto selectedEmployee;

    @RequestMapping("/Employee/addEmployee")
    public String addEmployeeForm(Model model) {
        model.addAttribute("newEmployee", new EmployeeDto());
        return "employee/addEmployeeHTML";
    }

    @RequestMapping(value = "/Employee/listEmployees", method = RequestMethod.POST)
    public String addEmployee(@ModelAttribute EmployeeDto employeeDto, Model model) {
        Employee newEmployee = employeeBuilderService.entityFromDto(employeeDto);
        employeeRepository.save(newEmployee);

        allEmployees(model);

        return LIST_EMPLOYEES;
    }

    @RequestMapping("/Employee/listEmployees")
    public String allEmployees(Model model) {
        activeEmployeeList = getActiveEmployeeList();
        model.addAttribute("activeEmployeesDtos", activeEmployeeList);

        inactiveEmployeeList = getInactiveEmployeeList();
        model.addAttribute("inactiveEmployeesDtos", inactiveEmployeeList);

        //LAMBDA for displaying counter for active employee active jobs
        Map<Long, Long> countOfActiveJobsForActiveEmployee = new HashMap<>();
        activeEmployeeList
                .forEach(employeeDto -> countOfActiveJobsForActiveEmployee.put(employeeDto.getId(), employeeDto.getWorkedJobs()
                        .stream()
                        .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.ACTIVE))
                        .count()));
        model.addAttribute("countOfActiveJobsForActiveEmployee", countOfActiveJobsForActiveEmployee);

        //LAMBDA for displaying counter for active employee completed jobs
        Map<Long, Long> countOfCompletedJobsForActiveEmployee = new HashMap<>();
        activeEmployeeList
                .stream()
                .forEach(employeeDto -> countOfCompletedJobsForActiveEmployee.put(employeeDto.getId(), employeeDto.getWorkedJobs()
                        .stream()
                        .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.COMPLETED))
                        .count()));
        model.addAttribute("countOfCompletedJobsForActiveEmployee", countOfCompletedJobsForActiveEmployee);

        //LAMBDA for displaying counter for inactive employee completed jobs
        Map<Long, Long> countOfCompletedJobsForInactiveEmployee = new HashMap<>();
        inactiveEmployeeList
                .stream()
                .forEach(employeeDto -> countOfCompletedJobsForInactiveEmployee.put(employeeDto.getId(), employeeDto.getWorkedJobs()
                        .stream()
                        .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.COMPLETED))
                        .count()));
        model.addAttribute("countOfCompletedJobsForInactiveEmployee", countOfCompletedJobsForInactiveEmployee);

        return "employee/employeesHTML";
    }

    @RequestMapping("Employee/{id}/move_Inactive")
    public String moveEmployeeInactive(@PathVariable Long id, Model model) {
        Employee employeeToMoveInactive = employeeBuilderService.selectEmployee(id);
        if (!employeeToMoveInactive.isAssignedForJobs()) {
            employeeToMoveInactive.setEmployeeStatus(EmployeeStatus.INACTIVE);
            employeeRepository.save(employeeToMoveInactive);
        }

        return LIST_EMPLOYEES;
    }

    @RequestMapping("Employee/{id}/show")
    public String getEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeBuilderService.selectEmployee(id);
        selectedEmployee = employeeBuilderService.dtoFromEntityWithJobs(employee);

        model.addAttribute("employee", selectedEmployee);

        return "employee/employeeHTML";
    }

    @RequestMapping("Employee/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        Employee employeeToDelete = employeeBuilderService.selectEmployee(id);
        for (Job workedJob : employeeToDelete.getWorkedJobs()) {
            if (workedJob.getJobStatus().equals(JobStatus.ACTIVE)) {
                employeeToDelete = null;
                break;
            } else {
                //empty
            }
        }

        if (employeeToDelete != null) {
            employeeRepository.delete(employeeToDelete);
        }

        return LIST_EMPLOYEES;
    }

    @RequestMapping("Employee/{id}/move_Active")
    public String moveEmployeeActive(@PathVariable Long id, Model model) {
        Employee employeeToMoveActive = employeeBuilderService.selectEmployee(id);
        employeeToMoveActive.setEmployeeStatus(EmployeeStatus.ACTIVE);
        employeeRepository.save(employeeToMoveActive);

        return LIST_EMPLOYEES;
    }

    @RequestMapping("Employee/{id}/edit")
    public String editEmployee(@PathVariable Long id, Model model) {
        Employee employeeToEdit = employeeBuilderService.selectEmployee(id);
        selectedEmployee = employeeBuilderService.dtoFromEntityWithJobs(employeeToEdit);

        model.addAttribute("employee", selectedEmployee);

        return "employee/updateEmployeeHTML";
    }

    @RequestMapping(value = "/Employee/updateEmployee", method = RequestMethod.POST)
    public String updateEmployee(@ModelAttribute EmployeeDto employeeDto, Model model) {
        Employee employeeToUpdate = employeeBuilderService.selectEmployee(selectedEmployee.getId());
        employeeToUpdate = employeeBuilderService.updateEntityFromDto(employeeDto, employeeToUpdate);
        employeeRepository.save(employeeToUpdate);

        allEmployees(model);

        return LIST_EMPLOYEES;
    }

    public List<EmployeeDto> getActiveEmployeeList() {
        activeEmployeeList = employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getEmployeeStatus().equals(EmployeeStatus.ACTIVE))
                .map(e -> employeeBuilderService.dtoFromEntityWithJobs(e))
                .collect(Collectors.toList());

        return activeEmployeeList;
    }

    public List<EmployeeDto> getInactiveEmployeeList() {
        inactiveEmployeeList = employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getEmployeeStatus().equals(EmployeeStatus.INACTIVE))
                .map(e -> employeeBuilderService.dtoFromEntityWithJobs(e))
                .collect(Collectors.toList());

        return inactiveEmployeeList;
    }
}
