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
import pl.sda.wrozki_chrzestne_v_2.employee.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class JobController {

    private static final String JOB_LIST = "redirect:/Job/listJobs";
    private JobRepository jobRepository;

    private JobBuilderService jobBuilderService;

    private EmployeeRepository employeeRepository;

    private EmployeeBuilderService employeeBuilderService;

    private EmployeeController employeeController;

    private ClientBuilderService clientBuilderService;

    private ClientController clientController;

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Autowired
    public void setJobBuilderService(JobBuilderService jobBuilderService) {
        this.jobBuilderService = jobBuilderService;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    public void setEmployeeBuilderService(EmployeeBuilderService employeeBuilderService) {
        this.employeeBuilderService = employeeBuilderService;
    }

    @Autowired
    public void setEmployeeController(EmployeeController employeeController) {
        this.employeeController = employeeController;
    }

    @Autowired
    public void setClientBuilderService(ClientBuilderService clientBuilderService) {
        this.clientBuilderService = clientBuilderService;
    }

    @Autowired
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    private List<JobDto> completedJobs = new ArrayList<>();
    private List<JobDto> uncompletedJobs = new ArrayList<>();
    private JobDto selectedJobDto;
    private List<EmployeeDto> assignedEmployeesForActiveJob = new ArrayList<>();
    private List<EmployeeDto> assignedEmployeesForCompletedJob = new ArrayList<>();
    private ClientDto selectedClientDto;

    @RequestMapping("/Job/addJob")
    public String addJobForm(Model model) {
        model.addAttribute("job", new JobDto());
        model.addAttribute("clients", clientController.getAllClients());
        return "job/addJobHTML";
    }

    @RequestMapping(value = "/Job/listJobs", method = RequestMethod.POST)
    public String addJob(@ModelAttribute JobDto jobDto, Model model) {
        Job newJob = jobBuilderService.entityFromDto(jobDto);
        jobRepository.save(newJob);

        allJobs(model);

        return JOB_LIST;
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
        Job job = jobBuilderService.selectJob(id);
        selectedJobDto = jobBuilderService.dtoFromEntity(job);

        Optional<JobDto> completedJobToShow = completedJobs
                .stream()
                .filter(jobDto -> jobDto.getId().equals(selectedJobDto.getId()))
                .findFirst();

        if (completedJobToShow.isPresent()) {
            selectedJobDto = completedJobToShow.get();
        } else {
            selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);
        }

        model.addAttribute("job", selectedJobDto);

        List<EmployeeDto> activeEmployeesDtos = employeeController.getActiveEmployeeList();
        model.addAttribute("employees", activeEmployeesDtos);

        return "job/jobHTML";
    }

    @GetMapping("Job/{id}/move")
    public String moveJobCompleted(@PathVariable Long id, Model model) {
        Job jobToMove = jobBuilderService.selectJob(id);
        selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(jobToMove);
        selectedJobDto.setJobStatus(JobStatus.COMPLETED);

        // lambda for set employee assigned for job as FALSE
        selectedJobDto.getEmployees()
                .stream()
                .filter(employee -> employee.getWorkedJobs()
                        .stream()
                        .allMatch(job -> job.getJobStatus().equals(JobStatus.COMPLETED)))
                .peek(employee -> employee.setAssignedForJobs(false))
                .collect(Collectors.toList());

        jobToMove = jobBuilderService.updateEntityFromDto(selectedJobDto, jobToMove);
        jobRepository.save(jobToMove);

        JobDto jobToMoveCompleted = jobBuilderService.dtoFromEntityWithEmployees(jobToMove);

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
            for (EmployeeDto assignedEmployeeForActiveJob : assignedEmployeesForActiveJob) {
                if (assignedEmployeeForActiveJob.getId().equals(assignedEmployeeForJobBeingCompleted.getId())) {
                    assignedEmployeesForCompletedJob.add(assignedEmployeeForActiveJob);
                    break;
                }
            }
        }
        assignedEmployeesForActiveJob.removeAll(assignedEmployeesForCompletedJob);

        model.addAttribute("job", jobToMoveCompleted);

        return JOB_LIST;

    }

    @RequestMapping("Job/{id}/edit")
    public String editJob(@PathVariable Long id, Model model) {
        Job job = jobBuilderService.selectJob(id);
        selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);

        model.addAttribute("selectedJobDto", selectedJobDto);
        model.addAttribute("sorts", SortOfJobs.values());

        List<ClientDto> clientsToChange = clientController.getAllClients();
        selectedClientDto = selectedJobDto.getClient();

        for (ClientDto clientDto : clientsToChange) {
            if (clientDto.getId().equals(selectedClientDto.getId())) {
                clientsToChange.remove(clientDto);
                break;
            }
        }

        model.addAttribute("clients", clientsToChange);

        return "job/updateJobHTML";
    }

    @RequestMapping(value = "/Job/updateJob/{idJob}", method = RequestMethod.POST)
    public String updateJob(@PathVariable Long idJob, Model model) {
        Job job = jobBuilderService.selectJob(idJob);
        selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);
        job = jobBuilderService.updateEntityFromDto(selectedJobDto, job);
        jobRepository.save(job);

        allJobs(model);

        return JOB_LIST;
    }

    @RequestMapping(value = "Job/selectClient/{id}", method = RequestMethod.POST)
    public String selectClient(@ModelAttribute JobDto jobDto, @ModelAttribute ClientDto clientDto, Model model) {
        Client client = clientBuilderService.selectClient(clientDto.getId());
        selectedClientDto = clientBuilderService.dtoFromEntity(client);
        jobDto.setClient(selectedClientDto);

        Job newJob = jobBuilderService.entityFromDtoWithUpdatingClient(jobDto, selectedClientDto);
        jobRepository.save(newJob);

        allJobs(model);

        return "redirect:/Job/" + newJob.getId() + "/edit";
    }

    @RequestMapping(value = "Job/{idJob}/selectClient/{id}", method = RequestMethod.POST)
    public String changeSelectedClient(@PathVariable Long idJob, @ModelAttribute ClientDto clientDto, Model model) {
        Client client = clientBuilderService.selectClient(clientDto.getId());
        selectedClientDto = clientBuilderService.dtoFromEntity(client);

        Job job = jobBuilderService.selectJob(idJob);
        JobDto editedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);
        editedJobDto.setClient(selectedClientDto);

        job = jobBuilderService.updateEntityFromDtoWithClient(editedJobDto, job);
        jobRepository.save(job);

        return "redirect:/Job/" + idJob + "/edit";
    }

    @RequestMapping("Job/{id}/assignEmployee")
    public String assignEmployee(@PathVariable Long id, Model model) {
        Job job = jobBuilderService.selectJob(id);
        selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);

        model.addAttribute("jobForAssign", selectedJobDto);

        List<EmployeeDto> activeEmployeesDtos = employeeController.getActiveEmployeeList();

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
    public String assignEmployeeForJob(@ModelAttribute EmployeeDto employeeDto, @PathVariable Long id, Model model) {
        Employee employeeToAssign = employeeBuilderService.selectEmployee(employeeDto.getId());
        employeeToAssign.setAssignedForJobs(true);

        Job job = jobBuilderService.selectJob(id);
        job.getEmployees().add(employeeToAssign);
        jobRepository.save(job);

        allJobs(model);

        return "redirect:/Job/" + selectedJobDto.getId() + "/assignEmployee";
    }

    @RequestMapping(value = "/Job/{id}/removeAssignedEmployee/{idEmployee}", method = RequestMethod.POST)
    public String removeAssignedEmployeeFromJob(@PathVariable Long id, @PathVariable Long idEmployee, Model model) {
        Employee removedEmployee = employeeBuilderService.selectEmployee(idEmployee);
        Job job = jobBuilderService.selectJob(id);

        List<Employee> assignedEmployeesForSelectedJob = job.getEmployees();

        //removing employee only from selectedJobDto
        for (Employee assignedEmployeeJob : assignedEmployeesForSelectedJob) {

            if (removedEmployee.getId().equals(assignedEmployeeJob.getId())) {
                assignedEmployeesForSelectedJob.remove(assignedEmployeeJob);
                break;
            }
        }

        jobRepository.save(job);

        // lambda for set employee assigned for job as FALSE
        if (removedEmployee.getWorkedJobs()
                .stream()
                .filter(job1 -> job1.getJobStatus().equals(JobStatus.ACTIVE))
                .collect(Collectors.toList())
                .isEmpty()) {
            removedEmployee.setAssignedForJobs(false);
            employeeRepository.save(removedEmployee);
        }

        allJobs(model);

        return "redirect:/Job/" + selectedJobDto.getId() + "/assignEmployee";
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
        return completedJobs;
    }

    public List<EmployeeDto> getAssignedEmployeesForActiveJob() {
        assignedEmployeesForActiveJob = employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getEmployeeStatus().equals(EmployeeStatus.ACTIVE) && employee.isAssignedForJobs())
                .map(employee -> employeeBuilderService.dtoFromEntityWithJobs(employee))
                .collect(Collectors.toList());

        return assignedEmployeesForActiveJob;
    }

    public List<EmployeeDto> getAssignedEmployeesForCompletedJob() {
        assignedEmployeesForCompletedJob = employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getWorkedJobs()
                        .stream()
                        .anyMatch(job -> job.getJobStatus().equals(JobStatus.COMPLETED)))
                .map(employee -> employeeBuilderService.dtoFromEntityWithJobs(employee))
                .collect(Collectors.toList());

        return assignedEmployeesForCompletedJob;
    }
}
