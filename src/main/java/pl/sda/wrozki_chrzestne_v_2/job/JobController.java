package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobBuilderService jobBuilderService;

    private List<Job> completedJobs = new ArrayList<>();

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
                .map(e -> jobBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        model.addAttribute("jobsDtos", jobsDtos);

        List<JobDto> completedJobsDto = completedJobs
                .stream()
                .map(e -> jobBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        model.addAttribute("completedJobsDto", completedJobsDto);

        return "job/jobsHTML";
    }

    @RequestMapping("Job/{id}/show")
    public String getJob(@PathVariable Long id, Model model) {
        Job selectedJob = jobBuilderService.selectJob(id);

        JobDto selectedJobDto = jobBuilderService.DtoFromEntity(selectedJob);

        model.addAttribute("job", selectedJobDto);

        return "job/jobHTML";
    }

    @GetMapping("Job/{id}/move")
    public String moveJobCompleted(@PathVariable Long id, Model model) {
        Job selectedJob = jobBuilderService.selectJob(id);
        JobDto selectedJobDto = jobBuilderService.DtoFromEntity(selectedJob);

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
}
