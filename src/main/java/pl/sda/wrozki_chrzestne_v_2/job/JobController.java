package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.client.Client;
import pl.sda.wrozki_chrzestne_v_2.client.ClientBuilderService;
import pl.sda.wrozki_chrzestne_v_2.client.ClientController;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;
import pl.sda.wrozki_chrzestne_v_2.employee.Employee;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeBuilderService;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeController;

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
    private EmployeeController employeeController;

    @Autowired
    private ClientBuilderService clientBuilderService;

    @Autowired
    private ClientController clientController;

    private List<JobDto> completedJobs = new ArrayList<>();
    private List<JobDto> uncompletedJobs = new ArrayList<>();
    private Job editedJob;
    private Job selectedJob;
    private List<Employee> assignedEmployeesForActiveJob = new ArrayList<>();
    private List<Employee> assignedEmployeesForCompletedJob = new ArrayList<>();
    private Client selectedClient;

    @RequestMapping("/Job/addJob")
    public String addJobForm(Model model) {
        model.addAttribute("job", new JobDto());
//        model.addAttribute("sorts", SortOfJobs.values());
        model.addAttribute("clients", clientController.getAllClients());
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

        uncompletedJobs = getUncompletedJobList();
        model.addAttribute("activeJobsDto", uncompletedJobs);

        completedJobs = getCompletedJobList();
        model.addAttribute("completedJobsDto", completedJobs);

        return "job/jobsHTML";
    }

    @RequestMapping("Job/{id}/show")
    public String getJob(@PathVariable Long id, Model model) {
        selectedJob = jobBuilderService.selectJob(id);
        JobDto selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(selectedJob);

        model.addAttribute("job", selectedJobDto);

        List<EmployeeDto> activeEmployeesDtos = employeeController.getActiveEmployeeList();
        model.addAttribute("employees", activeEmployeesDtos);

//        List<EmployeeDto> employeesDtos = employeesList
//                .stream()
//                .map(e -> employeeBuilderService.dtoFromEntityWithJobs(e))
//                .collect(Collectors.toList());
        return "job/jobHTML";
    }

    @GetMapping("Job/{id}/move")
    public String moveJobCompleted(@PathVariable Long id, Model model) {
        editedJob = jobBuilderService.selectJob(id);
        editedJob.setJobStatus(JobStatus.COMPLETED);

        // lambda for set employee assigned for job as FALSE

        editedJob.getEmployees()
                .stream()
                .filter(employee -> employee.getWorkedJobs()
                        .stream()
                        .allMatch(job -> job.getJobStatus().equals(JobStatus.COMPLETED)))
                .peek(employee -> employee.setAssignedForJobs(false))
                .collect(Collectors.toList());

        //.peek(employee -> employee.setAssignedForJobs(false));
//        editedJob.getEmployees()
//                .stream()
//                .forEach(employee -> employee.getWorkedJobs()
//                        .stream()
//                        .allMatch(job -> job.getJobStatus().equals(JobStatus.COMPLETED)));

        //.map(employee -> !employee.isAssignedForJobs().).collect(Collectors.toList());
        jobRepository.save(editedJob);

        JobDto jobToMoveCompleted = jobBuilderService.dtoFromEntityWithEmployees(editedJob);

        if (completedJobs.isEmpty()) {
            completedJobs.add(jobToMoveCompleted);
        } else {
            for (JobDto completedJob : completedJobs) {
                if (completedJob.getId().equals(jobToMoveCompleted.getId())) {
                    break;
                }
            }
            completedJobs.add(jobToMoveCompleted);
        }

        List<EmployeeDto> assignedEmployeesForJobBeingCompleted = jobToMoveCompleted.getEmployees();

        for (EmployeeDto assignedEmployeeForJobBeingCompleted : assignedEmployeesForJobBeingCompleted) {
            for (Employee assignedEmployeeForActiveJob : assignedEmployeesForActiveJob) {
                if (assignedEmployeeForActiveJob.getId().equals(assignedEmployeeForJobBeingCompleted.getId())) {
                    assignedEmployeesForCompletedJob.add(assignedEmployeeForActiveJob);
                    break;
                }
            }
        }
        assignedEmployeesForActiveJob.removeAll(assignedEmployeesForCompletedJob);

        model.addAttribute("job", jobToMoveCompleted);

        return "redirect:/Job/listJobs";

//        editedJob = jobBuilderService.selectJob(id);
//        editedJob.setJobStatus(JobStatus.COMPLETED);
//        jobRepository.save(editedJob);
//
//        assignedEmployeesForCompletedJob.addAll(editedJob.getEmployees());
//
//        assignedEmployeesForActiveJob.removeAll(editedJob.getEmployees());
//
//        allJobs(model);

//        JobDto editedJobDto = jobBuilderService.dtoFromEntityWithEmployees(editedJob);
//        model.addAttribute("job", editedJobDto);

    }

    @RequestMapping("Job/{id}/edit")
    public String editJob(@PathVariable Long id, Model model) {
        editedJob = jobBuilderService.selectJob(id);
        JobDto editedJobDto = jobBuilderService.dtoFromEntityWithEmployees(editedJob);

        model.addAttribute("editedJob", editedJobDto);
        model.addAttribute("sorts", SortOfJobs.values());

        List<ClientDto> clientsToChange = clientController.getAllClients();
        selectedClient = editedJob.getClient();

        for (ClientDto clientDto : clientsToChange) {
            if (clientDto.getId().equals(selectedClient.getId())) {
                clientsToChange.remove(clientDto);
                break;
            }
        }

        model.addAttribute("clients", clientsToChange);

        return "job/updateJobHTML";
    }

    @RequestMapping(value = "/Job/updateJob", method = RequestMethod.POST)
    public String updateJob(@ModelAttribute JobDto jobDto, Model model) {
        editedJob = jobBuilderService.updateEntityFromDto(jobDto, editedJob);
        jobRepository.save(editedJob);

        allJobs(model);

        return "redirect:/Job/listJobs";
    }

    @RequestMapping(value = "Job/selectClient/{id}", method = RequestMethod.POST)
    public String selectClient(@ModelAttribute JobDto jobDto, @ModelAttribute ClientDto clientDto, Model model) {
        selectedClient = clientBuilderService.selectClient(clientDto.getId());
        ClientDto selectedClientDto = clientBuilderService.dtoFromEntity(selectedClient);
        jobDto.setClient(selectedClientDto);

        Job newJob = jobBuilderService.entityFromDtoWithUpdatingClient(jobDto, selectedClientDto);
        jobRepository.save(newJob);

        allJobs(model);

        return "redirect:/Job/" + newJob.getId() + "/edit";
    }

    @RequestMapping(value = "Job/{idJob}/selectClient/{id}", method = RequestMethod.POST)
    public String changeSelectedClient(@PathVariable Long idJob, @ModelAttribute ClientDto clientDto, Model model) {
        selectedClient = clientBuilderService.selectClient(clientDto.getId());
        ClientDto selectedClientDto = clientBuilderService.dtoFromEntity(selectedClient);

        editedJob = jobBuilderService.selectJob(idJob);

        JobDto editedJobDto = jobBuilderService.dtoFromEntityWithEmployees(editedJob);
        editedJobDto.setClient(selectedClientDto);

        editedJob = jobBuilderService.updateEntityFromDtoWithClient(editedJobDto, editedJob);

        jobRepository.save(editedJob);

        return "redirect:/Job/" + idJob + "/edit";
    }

    @RequestMapping("Job/{id}/assignEmployee")
    public String assignEmployee(@PathVariable Long id, Model model) {
        selectedJob = jobBuilderService.selectJob(id);
        JobDto selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(selectedJob);

        model.addAttribute("jobForAssign", selectedJobDto);

        List<EmployeeDto> activeEmployeesDtos = employeeController.getActiveEmployeeList();
//        List<EmployeeDto> activeEmployeesDtos = employeesList
//                .stream()
//                .map(e -> employeeBuilderService.dtoFromEntityWithJobs(e))
//                .collect(Collectors.toList());

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
        Employee employeeToAssign = employeeBuilderService.selectEmployee(employeeDto.getId());
        employeeToAssign.setAssignedForJobs(true);
        selectedJob.getEmployees().add(employeeToAssign);
        jobRepository.save(selectedJob);

        assignedEmployeesForActiveJob.add(employeeToAssign);

        allJobs(model);

        return "redirect:/Job/" + selectedJob.getId() + "/assignEmployee";
    }

    @RequestMapping(value = "/Job/{id}/removeAssignedEmployee/{idEmployee}", method = RequestMethod.POST)
    public String removeAssignedEmployeeFromJob(@PathVariable Long idEmployee, Model model) {
        Employee removedEmployee = employeeBuilderService.selectEmployee(idEmployee);
        List<Employee> assignedEmployeesForSelectedJob = selectedJob.getEmployees();

//        for (Employee assignedEmployeeJob : assignedEmployeesForSelectedJob) {
//
//            if (removedEmployee.getId().equals(assignedEmployeeJob.getId())) {
//                assignedEmployeesForSelectedJob.remove(assignedEmployeeJob);
//                break;
//            }
//        }
//
//
//        for (Employee assignedEmployee : assignedEmployeesForActiveJob) {
//            if (removedEmployee.getId().equals(assignedEmployee.getId())) {
//                assignedEmployeesForActiveJob.remove(assignedEmployee);
//                break;
//            }
//        }

        jobRepository.save(selectedJob);

        // lambda for set employee assigned for job as FALSE

        if (getUncompletedJobList()
                .stream()
                .filter(jobDto -> jobDto.getEmployees()
                        .stream()
                        .anyMatch(employeeDto -> employeeDto.getId().equals(removedEmployee.getId())))
                .collect(Collectors.toList()).isEmpty()) {
            removedEmployee.setAssignedForJobs(false);
        }

        allJobs(model);

        return "redirect:/Job/" + selectedJob.getId() + "/assignEmployee";
    }

    public List<JobDto> getUncompletedJobList() {
        uncompletedJobs = jobRepository.findAll()
                .stream()
                .filter(job -> job.getJobStatus().equals(JobStatus.ACTIVE))
                .map(e -> jobBuilderService.dtoFromEntityWithEmployees(e))
                .collect(Collectors.toList());

        return uncompletedJobs;
    }

    public List<JobDto> getCompletedJobList() {
        completedJobs = jobRepository.findAll()
                .stream()
                .filter(job -> job.getJobStatus().equals(JobStatus.COMPLETED))
                .map(e -> jobBuilderService.dtoFromEntityWithEmployees(e))
                .collect(Collectors.toList());

        return completedJobs;
    }

    public List<Employee> getAssignedEmployeesForActiveJob() {
        return assignedEmployeesForActiveJob;
    }

    public List<Employee> getAssignedEmployeesForCompletedJob() {
        return assignedEmployeesForCompletedJob;
    }

}
