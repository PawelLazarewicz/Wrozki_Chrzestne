package pl.sda.wrozki_chrzestne_v_2.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;
import pl.sda.wrozki_chrzestne_v_2.job.Job;
import pl.sda.wrozki_chrzestne_v_2.job.JobController;
import pl.sda.wrozki_chrzestne_v_2.job.JobStatus;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeBuilderService employeeBuilderService;

    @Autowired
    private JobController jobController;

    private List<EmployeeDto> inactiveEmployeeList = new ArrayList<>();
    private List<EmployeeDto> activeEmployeeList = new ArrayList<>();
    private EmployeeDto selectedEmployee;
    private Map<Long, Long> assignedEmployeesForActiveJobMap = new HashMap<>();
    private Map<Long, Long> assignedEmployeesForCompletedJobMap = new HashMap<>();

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
        activeEmployeeList = getActiveEmployeeList();
        model.addAttribute("activeEmployeesDtos", activeEmployeeList);

        inactiveEmployeeList = getInactiveEmployeeList();
        model.addAttribute("inactiveEmployeesDtos", inactiveEmployeeList);

        //LOOPS for displaying counter of employee active jobs
        List<EmployeeDto> assignedEmployeesForActiveJob = jobController.getAssignedEmployeesForActiveJob();
        for (EmployeeDto employee : activeEmployeeList) {
            assignedEmployeesForActiveJobMap.put(employee.getId(), 0L);
        }

        for (EmployeeDto employee : activeEmployeeList) {
            for (EmployeeDto assignedEmployeeForActiveJob : assignedEmployeesForActiveJob) {
                if (employee.getId().equals(assignedEmployeeForActiveJob.getId())) {
                    for (Map.Entry entry : assignedEmployeesForActiveJobMap.entrySet()) {
                        if (entry.getKey().equals(employee.getId())) {
                            Long employeeActiveJobs = employee.getWorkedJobs()
                                    .stream()
                                    .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.ACTIVE))
                                    .count();
                            assignedEmployeesForActiveJobMap.replace(employee.getId(), employeeActiveJobs);
                        }
                    }
                }
            }
        }

        model.addAttribute("assignedEmployeesForActiveJobMap", assignedEmployeesForActiveJobMap);

        //LOOPS for displaying counter of employee completed jobs
        List<EmployeeDto> assignedEmployeesForCompletedJob = jobController.getAssignedEmployeesForCompletedJob();
        for (EmployeeDto employee : activeEmployeeList) {
            assignedEmployeesForCompletedJobMap.put(employee.getId(), 0L);
        }

        for (EmployeeDto employee : activeEmployeeList) {
            for (EmployeeDto assignedEmployeeForCompletedJob : assignedEmployeesForCompletedJob) {
                if (employee.getId().equals(assignedEmployeeForCompletedJob.getId())) {
                    for (Map.Entry entry : assignedEmployeesForCompletedJobMap.entrySet()) {
                        if (entry.getKey().equals(employee.getId())) {
                            Long employeeCompletedJobs = employee.getWorkedJobs()
                                    .stream()
                                    .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.COMPLETED))
                                    .count();
                            assignedEmployeesForCompletedJobMap.replace(employee.getId(), employeeCompletedJobs);
                        }
                    }
                }
            }
        }

        model.addAttribute("assignedEmployeesForCompletedJobMap", assignedEmployeesForCompletedJobMap);

        return "employee/employeesHTML";
    }

    @RequestMapping("Employee/{id}/move_Inactive")
    public String moveEmployeeInactive(@PathVariable Long id, Model model) {
        Employee employeeToMoveInactive = employeeBuilderService.selectEmployee(id);
        if (!employeeToMoveInactive.isAssignedForJobs()) {
            employeeToMoveInactive.setEmployeeStatus(EmployeeStatus.INACTIVE);
            employeeRepository.save(employeeToMoveInactive);
        }

        return "redirect:/Employee/listEmployees";
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

        return "redirect:/Employee/listEmployees";
    }

    @RequestMapping("Employee/{id}/move_Active")
    public String moveEmployeeActive(@PathVariable Long id, Model model) {
        Employee employeeToMoveActive = employeeBuilderService.selectEmployee(id);
        employeeToMoveActive.setEmployeeStatus(EmployeeStatus.ACTIVE);
        employeeRepository.save(employeeToMoveActive);

        return "redirect:/Employee/listEmployees";
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

        return "redirect:/Employee/listEmployees";
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
