package pl.sda.wrozki_chrzestne_v_2.client;

import lombok.AllArgsConstructor;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;

@AllArgsConstructor
public class ClientFacade {

    private ClientBuilderService clientBuilderService;
    private ClientRepository clientRepository;

    public void addClient(ClientDto clientDto) {

        Client newClient = clientBuilderService.entityFromDto(clientDto);
        clientRepository.save(newClient);

        //return clientBuilderService.dtoFromEntityWithJobs(savedClient);
    }
}
