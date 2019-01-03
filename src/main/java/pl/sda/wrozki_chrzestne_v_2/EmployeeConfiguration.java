package pl.sda.wrozki_chrzestne_v_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeConfiguration {

    @Bean
    EmployeeBuilderService employeeBuilderService(){
        return new EmployeeBuilderService();
    }
}
