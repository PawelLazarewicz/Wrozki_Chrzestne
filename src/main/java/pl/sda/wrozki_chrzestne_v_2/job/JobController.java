package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;
import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;

import java.util.ArrayList;
import java.util.List;

@Controller
public class JobController {

    private JobFacade jobFacade;

    @Autowired
    public void setJobFacade(JobFacade jobFacade) {
        this.jobFacade = jobFacade;
    }

    private static final String JOB_LIST = "redirect:/Job/listJobs";

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

        allJobs(model);

        return JOB_LIST;
    }

    @RequestMapping("/Job/listJobs")
    public String allJobs(Model model) {
        model.addAttribute("activeJobsDto", jobFacade.getUncompletedJobList());

        model.addAttribute("completedJobsDto", jobFacade.getCompletedJobList());

        return "job/jobsHTML";
    }

    @RequestMapping("Job/{id}/show")
    public String getJob(@PathVariable Long id, Model model) {
        model.addAttribute("job", jobFacade.getJob(id));

        model.addAttribute("employees", jobFacade.getActiveEmployeeList());

        return "job/jobHTML";
    }

    @GetMapping("Job/{id}/move")
    public String moveJobCompleted(@PathVariable Long id, Model model) {
        jobFacade.moveJobCompleted(id);

        return JOB_LIST;
    }

    @RequestMapping("Job/{id}/edit")
    public String editJob(@PathVariable Long id, Model model) {
        selectedJobDto = jobFacade.selectJob(id);

        model.addAttribute("selectedJobDto", selectedJobDto);
        model.addAttribute("sorts", SortOfJobs.values());
        model.addAttribute("clients", jobFacade.getClientsToChange(selectedJobDto));

        return "job/updateJobHTML";
    }

    @RequestMapping(value = "/Job/updateJob", method = RequestMethod.POST)
    public String updateJob(@ModelAttribute JobDto jobDto, Model model) {
        jobFacade.updateJob(jobDto, selectedJobDto);

        allJobs(model);

        return JOB_LIST;
    }

    @RequestMapping(value = "Job/selectClient/{id}", method = RequestMethod.POST)
    public String selectClient(@ModelAttribute JobDto jobDto, @ModelAttribute ClientDto clientDto, Model model) {
        Long newJobId = jobFacade.selectClient(clientDto, jobDto);

        allJobs(model);

        return "redirect:/Job/" + newJobId + "/edit";
    }

    @RequestMapping(value = "Job/{idJob}/selectClient/{id}", method = RequestMethod.POST)
    public String changeSelectedClient(@PathVariable Long idJob, @ModelAttribute ClientDto clientDto, Model model) {
        jobFacade.changeSelectedClient(idJob, clientDto);

        return "redirect:/Job/" + idJob + "/edit";
    }

    @RequestMapping("Job/{id}/assignEmployee")
    public String assignEmployee(@PathVariable Long id, Model model) {
        selectedJobDto = jobFacade.selectJob(id);
        model.addAttribute("jobForAssign", selectedJobDto);

        List<EmployeeDto> employeesDtoAvailableToAssign = jobFacade.getEmployeesDtoAvailableToAssign(selectedJobDto);
        model.addAttribute("employeesDtoAvailableToAssign", employeesDtoAvailableToAssign);

        return "job/assignEmployeeHTML";
    }

    @RequestMapping(value = "/Job/{id}/assigningEmployee", method = RequestMethod.POST)
    public String assignEmployeeForJob(@ModelAttribute EmployeeDto employeeDto, @PathVariable Long id, Model model) {
        jobFacade.assignEmployeeForJob(employeeDto, id);

        allJobs(model);

        return "redirect:/Job/" + id + "/assignEmployee";
    }

    @RequestMapping(value = "/Job/{id}/removeAssignedEmployee/{idEmployee}", method = RequestMethod.POST)
    public String removeAssignedEmployeeFromJob(@PathVariable Long id, @PathVariable Long idEmployee, Model model) {
        jobFacade.removeEmployeeFromJob(id, idEmployee);

        jobFacade.setEmployeeAssignedAsFalse(idEmployee);

        allJobs(model);

        return "redirect:/Job/" + id + "/assignEmployee";
    }

    public List<JobDto> getCompletedJobsField() {
        return completedJobs;
    }
}