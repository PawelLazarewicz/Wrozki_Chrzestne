package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sda.wrozki_chrzestne_v_2.client.ClientBuilderService;
import pl.sda.wrozki_chrzestne_v_2.client.ClientController;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeBuilderService;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeController;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeRepository;

@Configuration
public class JobConfiguration {

    @Bean
    JobBuilderService jobBuilderService(){
        return new JobBuilderService();
    }

    @Bean
    JobFacade jobFacade(JobRepository jobRepository, JobBuilderService jobBuilderService, JobController jobController, EmployeeController employeeController, ClientController clientController, ClientBuilderService clientBuilderService, EmployeeBuilderService employeeBuilderService, EmployeeRepository employeeRepository){
        return new JobFacade(jobRepository, jobBuilderService, jobController, employeeController, clientController, clientBuilderService, employeeBuilderService, employeeRepository);
    }
}
