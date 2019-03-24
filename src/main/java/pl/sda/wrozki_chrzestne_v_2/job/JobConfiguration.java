package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

    @Bean
    JobBuilderService jobBuilderService(){
        return new JobBuilderService();
    }

    @Bean
    JobFacade jobFacade(JobRepository jobRepository, JobBuilderService jobBuilderService){
        return new JobFacade(jobRepository, jobBuilderService);
    }
}
