package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping("Job/listJobs")
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
}
