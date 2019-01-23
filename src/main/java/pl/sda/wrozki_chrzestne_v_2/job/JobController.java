package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;
import pl.sda.wrozki_chrzestne_v_2.employee.Employee;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeBuilderService;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeController;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobBuilderService jobBuilderService;

    @Autowired
    private EmployeeBuilderService employeeBuilderService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeController employeeController;

    private List<Job> completedJobs = new ArrayList<>();
    private Job editedJob;
    private Job selectedJob;

    @RequestMapping("/Job/addJob")
    public String addJobForm(Model model) {
        model.addAttribute("job", new JobDto());
        return "job/addJobHTML";
    }

    @RequestMapping(value = "/Job/listJobs", method = RequestMethod.POST)
    public String addJob(@ModelAttribute JobDto jobDto, Model model) {
        Job newJob = jobBuilderService.entityFromDto(jobDto);
        jobRepository.save(newJob);

        allJobs(model);

        return "redirect:/Job/listJobs";
    }

    @RequestMapping("/Job/listJobs")
    public String allJobs(Model model) {
        List<Job> jobs = jobRepository.findAll();

        for (int i = 0; i < jobs.size(); i++) {
            for (int j = 0; j < completedJobs.size(); j++) {
                if ((jobs.get(i).getId()).equals(completedJobs.get(j).getId())) {
                    jobs.remove(jobs.get(i));
                }
            }
        }

        List<JobDto> jobsDtos = jobs
                .stream()
                .map(e -> jobBuilderService.dtoFromEntityWithEmployees(e))
                .collect(Collectors.toList());

        model.addAttribute("jobsDtos", jobsDtos);

        List<JobDto> completedJobsDto = completedJobs
                .stream()
                .map(e -> jobBuilderService.dtoFromEntityWithEmployees(e))
                .collect(Collectors.toList());

        model.addAttribute("completedJobsDto", completedJobsDto);

        return "job/jobsHTML";
    }

    @RequestMapping("Job/{id}/show")
    public String getJob(@PathVariable Long id, Model model) {
        selectedJob = jobBuilderService.selectJob(id);
        JobDto selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(selectedJob);

        model.addAttribute("job", selectedJobDto);

        List<Employee> employeesList = employeeController.getActiveEmployeeList();
        List<EmployeeDto> employeesDtos = employeesList
                .stream()
                .map(e -> employeeBuilderService.dtoFromEntityWithJobs(e))
                .collect(Collectors.toList());

        model.addAttribute("employees", employeesDtos);

        return "job/jobHTML";
    }

    @GetMapping("Job/{id}/move")
    public String moveJobCompleted(@PathVariable Long id, Model model) {
        Job selectedJob = jobBuilderService.selectJob(id);
        JobDto selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(selectedJob);

        if (completedJobs.isEmpty()) {
            completedJobs.add(selectedJob);
        } else {
            for (int i = 0; i < completedJobs.size(); i++) {
                if (!completedJobs.get(i).getId().equals(selectedJob.getId())) {
                    completedJobs.add(selectedJob);
                }
            }
        }

        model.addAttribute("job", selectedJobDto);

        return "job/jobHTML";
    }

    @RequestMapping("Job/{id}/edit")
    public String editJob(@PathVariable Long id, Model model) {
        editedJob = jobBuilderService.selectJob(id);
        JobDto editedJobDto = jobBuilderService.dtoFromEntityWithEmployees(editedJob);

        model.addAttribute("editedJob", editedJobDto);

        return "job/updateJobHTML";
    }

    @RequestMapping(value = "/Job/updateJob", method = RequestMethod.POST)
    public String updateJob(@ModelAttribute JobDto jobDto, Model model) {
        editedJob = jobBuilderService.updateEntityFromDto(jobDto, editedJob);
        jobRepository.save(editedJob);

        allJobs(model);

        return "redirect:/Job/listJobs";
    }

    @RequestMapping("Job/{id}/assignEmployee")
    public String assignEmployee(@PathVariable Long id, Model model) {
        selectedJob = jobBuilderService.selectJob(id);
        JobDto selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(selectedJob);

        model.addAttribute("jobForAssign", selectedJobDto);

        List<Employee> employeesList = employeeController.getActiveEmployeeList();
        List<EmployeeDto> activeEmployeesDtos = employeesList
                .stream()
                .map(e -> employeeBuilderService.dtoFromEntityWithJobs(e))
                .collect(Collectors.toList());

        List<EmployeeDto> alreadyAssignedEmployeesDto = selectedJobDto.getEmployees();
        List<EmployeeDto> employeesDtoAvailableToAssign = new ArrayList<>(activeEmployeesDtos);


        for (EmployeeDto employeeAssigned : alreadyAssignedEmployeesDto) {
            if (!employeesDtoAvailableToAssign.isEmpty()) {
                for (EmployeeDto employeeAvailableToAssign : employeesDtoAvailableToAssign) {
                    if (employeeAssigned.getId().equals(employeeAvailableToAssign.getId())) {
                        employeesDtoAvailableToAssign.remove(employeeAvailableToAssign);
                        break;
                    }
                }
            } else {
                employeesDtoAvailableToAssign = new ArrayList<>();
            }

        }

        model.addAttribute("employeesDtoAvailableToAssign", employeesDtoAvailableToAssign);

        return "job/assignEmployeeHTML";
    }

    @RequestMapping(value = "/Job/{id}/assigningEmployee", method = RequestMethod.POST)
    public String assignEmployeeForJob(@ModelAttribute EmployeeDto employeeDto, Model model) {
        selectedJob.getEmployees().add(employeeBuilderService.selectEmployee(employeeDto.getId()));
        jobRepository.save(selectedJob);

        allJobs(model);

        return "redirect:/Job/" + selectedJob.getId() + "/show";
    }
}
