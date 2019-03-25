package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.client.Client;
import pl.sda.wrozki_chrzestne_v_2.client.ClientBuilderService;
import pl.sda.wrozki_chrzestne_v_2.client.ClientController;
import pl.sda.wrozki_chrzestne_v_2.client.ClientFacade;
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

    private JobFacade jobFacade;

    @Autowired
    public void setJobFacade(JobFacade jobFacade) {
        this.jobFacade = jobFacade;
    }

    private JobBuilderService jobBuilderService;

    private EmployeeBuilderService employeeBuilderService;

    @Autowired
    public void setJobBuilderService(JobBuilderService jobBuilderService) {
        this.jobBuilderService = jobBuilderService;
    }

    @Autowired
    public void setEmployeeBuilderService(EmployeeBuilderService employeeBuilderService) {
        this.employeeBuilderService = employeeBuilderService;
    }

    //
    //    private EmployeeRepository employeeRepository;
    //
    //    private EmployeeController employeeController;
    //
    //    private ClientBuilderService clientBuilderService;
    //
    //    private ClientController clientController;
    //

//    private JobRepository jobRepository;

//    private ClientFacade clientFacade;

//    @Autowired
//    public void setJobRepository(JobRepository jobRepository) {
//        this.jobRepository = jobRepository;
//    }
//
//    @Autowired
//    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }
//
//    @Autowired
//    public void setEmployeeController(EmployeeController employeeController) {
//        this.employeeController = employeeController;
//    }
//
//    @Autowired
//    public void setClientBuilderService(ClientBuilderService clientBuilderService) {
//        this.clientBuilderService = clientBuilderService;
//    }
//
//    @Autowired
//    public void setClientController(ClientController clientController) {
//        this.clientController = clientController;
//    }

//    @Autowired
//    public void setClientFacade(ClientFacade clientFacade) {
//        this.clientFacade = clientFacade;
//    }
//
    //    private List<JobDto> uncompletedJobs = new ArrayList<>();
    //    private ClientDto selectedClientDto;

    private List<JobDto> completedJobs = new ArrayList<>();
    private JobDto selectedJobDto;

    @RequestMapping("/Job/addJob")
    public String addJobForm(Model model) {
        model.addAttribute("job", new JobDto());
        model.addAttribute("clients", jobFacade.getClients());
        return "job/addJobHTML";
    }

    @RequestMapping(value = "/Job/listJobs", method = RequestMethod.POST)
    public String addJob(@ModelAttribute JobDto jobDto, Model model) {
        jobFacade.addJob(jobDto);
//        Job newJob = jobBuilderService.entityFromDto(jobDto);
//        jobRepository.save(newJob);

        allJobs(model);

        return JOB_LIST;
    }

    @RequestMapping("/Job/listJobs")
    public String allJobs(Model model) {

//        uncompletedJobs = getUncompletedJobList();
        model.addAttribute("activeJobsDto", jobFacade.getUncompletedJobList());

//        completedJobs = getCompletedJobList();
        model.addAttribute("completedJobsDto", jobFacade.getCompletedJobList());

        return "job/jobsHTML";
    }

    @RequestMapping("Job/{id}/show")
    public String getJob(@PathVariable Long id, Model model) {
//        Job job = jobBuilderService.selectJob(id);
//        //selectedJobDto = jobBuilderService.dtoFromEntity(job);
//
//        Optional<JobDto> completedJobToShow = completedJobs
//                .stream()
//                .filter(jobDto -> jobDto.getId().equals(selectedJobDto.getId()))
//                .findFirst();
//
//        if (completedJobToShow.isPresent()) {
//            selectedJobDto = completedJobToShow.get();
//        } else {
//            selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);
//        }

        model.addAttribute("job", jobFacade.getJob(id));

//        List<EmployeeDto> activeEmployeesDtos = employeeController.getActiveEmployeeList();
        model.addAttribute("employees", jobFacade.getActiveEmployeeList());

        return "job/jobHTML";
    }

    @GetMapping("Job/{id}/move")
    public String moveJobCompleted(@PathVariable Long id, Model model) {
        jobFacade.moveJobCompleted(id);
//        Job jobToMove = jobBuilderService.selectJob(id);
//        selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(jobToMove);
//        selectedJobDto.setJobStatus(JobStatus.COMPLETED);
//
//        // lambda for set employee assigned for job as FALSE
//        selectedJobDto.getEmployees()
//                .stream()
//                .filter(employee -> employee.getWorkedJobs()
//                        .stream()
//                        .allMatch(job -> job.getJobStatus().equals(JobStatus.COMPLETED)))
//                .peek(employee -> employee.setAssignedForJobs(false))
//                .collect(Collectors.toList());
//
//        jobToMove = jobBuilderService.updateEntityFromDto(selectedJobDto, jobToMove);
//        jobRepository.save(jobToMove);
//
//        JobDto jobToMoveCompleted = jobBuilderService.dtoFromEntityWithEmployees(jobToMove);
//
//        if (completedJobs.isEmpty()) {
//            completedJobs.add(jobToMoveCompleted);
//        } else {
//            for (JobDto completedJob : completedJobs) {
//                if (completedJob.getId().equals(jobToMoveCompleted.getId())) {
//                    break;
//                }
//            }
//            completedJobs.add(jobToMoveCompleted);
//        }
        return JOB_LIST;
    }

    @RequestMapping("Job/{id}/edit")
    public String editJob(@PathVariable Long id, Model model) {
        Job job = jobBuilderService.selectJob(id);
        selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);

        model.addAttribute("selectedJobDto", selectedJobDto);
        model.addAttribute("sorts", SortOfJobs.values());

        model.addAttribute("clients", jobFacade.getClientsToChange(selectedJobDto));

        return "job/updateJobHTML";
    }

    @RequestMapping(value = "/Job/updateJob", method = RequestMethod.POST)
    public String updateJob(@ModelAttribute JobDto jobDto, Model model) {
        jobFacade.updateJob(jobDto, selectedJobDto);
//        Job job = jobBuilderService.selectJob(selectedJobDto.getId());
//        jobDto.setClient(selectedJobDto.getClient());
//        job = jobBuilderService.updateEntityFromDto(jobDto, job);
//        jobRepository.save(job);

        allJobs(model);

        return JOB_LIST;
    }

    @RequestMapping(value = "Job/selectClient/{id}", method = RequestMethod.POST)
    public String selectClient(@ModelAttribute JobDto jobDto, @ModelAttribute ClientDto clientDto, Model model) {
//        Client client = clientBuilderService.selectClient(clientDto.getId());
//        selectedClientDto = clientBuilderService.dtoFromEntity(client);
//        jobDto.setClient(selectedClientDto);
        Long newJobId = jobFacade.selectClient(clientDto, jobDto);

//        Job newJob = jobBuilderService.entityFromDtoWithUpdatingClient(jobDto, selectedClientDto);
//        jobRepository.save(newJob);

        allJobs(model);

        return "redirect:/Job/" + newJobId + "/edit";
    }

    @RequestMapping(value = "Job/{idJob}/selectClient/{id}", method = RequestMethod.POST)
    public String changeSelectedClient(@PathVariable Long idJob, @ModelAttribute ClientDto clientDto, Model model) {
        jobFacade.changeSelectedClient(idJob, clientDto);
//        Client client = clientBuilderService.selectClient(clientDto.getId());
//        selectedClientDto = clientBuilderService.dtoFromEntity(client);
//
//        Job job = jobBuilderService.selectJob(idJob);
//        JobDto editedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);
//        editedJobDto.setClient(selectedClientDto);
//
//        job = jobBuilderService.updateEntityFromDto(editedJobDto, job);
//        jobRepository.save(job);

        return "redirect:/Job/" + idJob + "/edit";
    }

    @RequestMapping("Job/{id}/assignEmployee")
    public String assignEmployee(@PathVariable Long id, Model model) {
        Job job = jobBuilderService.selectJob(id);
        selectedJobDto = jobBuilderService.dtoFromEntityWithEmployees(job);

        model.addAttribute("jobForAssign", selectedJobDto);

//        List<EmployeeDto> activeEmployeesDtos = employeeController.getActiveEmployeeList();
//
//        List<EmployeeDto> alreadyAssignedEmployeesDto = selectedJobDto.getEmployees();
//        List<EmployeeDto> employeesDtoAvailableToAssign = new ArrayList<>(activeEmployeesDtos);
//
//        for (EmployeeDto employeeAssigned : alreadyAssignedEmployeesDto) {
//            if (!employeesDtoAvailableToAssign.isEmpty()) {
//                for (EmployeeDto employeeAvailableToAssign : employeesDtoAvailableToAssign) {
//                    if (employeeAssigned.getId().equals(employeeAvailableToAssign.getId())) {
//                        employeesDtoAvailableToAssign.remove(employeeAvailableToAssign);
//                        break;
//                    }
//                }
//            } else {
//                employeesDtoAvailableToAssign = new ArrayList<>();
//            }
//        }
        List<EmployeeDto> employeesDtoAvailableToAssign = jobFacade.getEmployeesDtoAvailableToAssign(selectedJobDto);
        model.addAttribute("employeesDtoAvailableToAssign", employeesDtoAvailableToAssign);

        return "job/assignEmployeeHTML";
    }

    @RequestMapping(value = "/Job/{id}/assigningEmployee", method = RequestMethod.POST)
    public String assignEmployeeForJob(@ModelAttribute EmployeeDto employeeDto, @PathVariable Long id, Model model) {
//        Employee employeeToAssign = employeeBuilderService.selectEmployee(employeeDto.getId());
//        employeeToAssign.setAssignedForJobs(true);
//
//        Job job = jobBuilderService.selectJob(id);
//        job.getEmployees().add(employeeToAssign);
//        jobRepository.save(job);
        jobFacade.assignEmployeeForJob(employeeDto, id);

        allJobs(model);

        return "redirect:/Job/" + id + "/assignEmployee";
    }

    @RequestMapping(value = "/Job/{id}/removeAssignedEmployee/{idEmployee}", method = RequestMethod.POST)
    public String removeAssignedEmployeeFromJob(@PathVariable Long id, @PathVariable Long idEmployee, Model model) {
        Employee removedEmployee = employeeBuilderService.selectEmployee(idEmployee);
        Job job = jobBuilderService.selectJob(id);

        jobFacade.removeEmployeeFromJob(job, removedEmployee);

//        List<Employee> assignedEmployeesForSelectedJob = job.getEmployees();
//
//        //removing employee only from selectedJobDto
//        for (Employee assignedEmployeeJob : assignedEmployeesForSelectedJob) {
//
//            if (removedEmployee.getId().equals(assignedEmployeeJob.getId())) {
//                assignedEmployeesForSelectedJob.remove(assignedEmployeeJob);
//                break;
//            }
//        }
//
//        jobRepository.save(job);

        jobFacade.setEmployeeAssignedAsFalse(removedEmployee);

//        // lambda for set employee assigned for job as FALSE
//        if (removedEmployee.getWorkedJobs()
//                .stream()
//                .filter(job1 -> job1.getJobStatus().equals(JobStatus.ACTIVE))
//                .collect(Collectors.toList())
//                .isEmpty()) {
//            removedEmployee.setAssignedForJobs(false);
//            employeeRepository.save(removedEmployee);
//        }

        allJobs(model);

        return "redirect:/Job/" + selectedJobDto.getId() + "/assignEmployee";
    }

//    public List<JobDto> getUncompletedJobList() {
//        uncompletedJobs = jobRepository.findAll()
//                .stream()
//                .filter(job -> job.getJobStatus().equals(JobStatus.ACTIVE))
//                .map(e -> jobBuilderService.dtoFromEntityWithEmployees(e))
//                .collect(Collectors.toList());
//
//        return uncompletedJobs;
//    }

    public List<JobDto> getCompletedJobsField() {
        return completedJobs;
    }
}
