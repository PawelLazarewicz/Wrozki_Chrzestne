package pl.sda.wrozki_chrzestne_v_2.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    ClientBuilderService clientBuilderService(){
        return new ClientBuilderService();
    }

    @Bean
    ClientFacade clientFacade(ClientBuilderService clientBuilderService, ClientRepository clientRepository){
        return new ClientFacade(clientBuilderService, clientRepository);
    }
}
