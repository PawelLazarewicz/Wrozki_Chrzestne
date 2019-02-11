package pl.sda.wrozki_chrzestne_v_2.client;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;
import pl.sda.wrozki_chrzestne_v_2.job.Job;
import pl.sda.wrozki_chrzestne_v_2.job.JobBuilderService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientBuilderService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private JobBuilderService jobBuilderService;

    public Client entityFromDto(ClientDto clientDto) {
        Client client = new Client();

        client.setId(null);
        client.setName(clientDto.getName());
        client.setLastName(clientDto.getLastName());
        client.setCity(clientDto.getCity());
        client.setAddress(clientDto.getAddress());
        client.setPostalCode(clientDto.getPostalCode());
        client.setTelephoneNumber(clientDto.getTelephoneNumber());
        client.setMail(clientDto.getMail());

        List<Job> jobs = clientDto.getJobs()
                .stream()
                .map(e -> jobBuilderService.entityFromDto(e))
                .collect(Collectors.toList());

        client.setJobs(jobs);

        return client;
    }

    public ClientDto dtoFromEntity(Client client) {
        ClientDto clientDto = new ClientDto();

        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setLastName(client.getLastName());
        clientDto.setCity(client.getCity());
        clientDto.setAddress(client.getAddress());
        clientDto.setPostalCode(client.getPostalCode());
        clientDto.setTelephoneNumber(client.getTelephoneNumber());
        clientDto.setMail(client.getMail());

        return clientDto;
    }

    public ClientDto dtoFromEntityWithJobs(Client client) {
        ClientDto clientDto = new ClientDto();

        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setLastName(client.getLastName());
        clientDto.setCity(client.getCity());
        clientDto.setAddress(client.getAddress());
        clientDto.setPostalCode(client.getPostalCode());
        clientDto.setTelephoneNumber(client.getTelephoneNumber());
        clientDto.setMail(client.getMail());

        List<JobDto> jobsDto = client.getJobs()
                .stream()
                .map(e -> jobBuilderService.dtoFromEntity(e))
                .collect(Collectors.toList());

        clientDto.setJobs(jobsDto);

        return clientDto;
    }

    public Client selectClient(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        Client client = optionalClient.get();

        return client;
    }

    public Client updateEntityFromDto(ClientDto clientDto, Client client) {

        client.setId(client.getId());
        client.setName(clientDto.getName());
        client.setLastName(clientDto.getLastName());
        client.setCity(clientDto.getCity());
        client.setAddress(clientDto.getAddress());
        client.setPostalCode(clientDto.getPostalCode());
        client.setTelephoneNumber(clientDto.getTelephoneNumber());
        client.setMail(clientDto.getMail());

        List<Job> jobs = clientDto.getJobs()
                .stream()
                .map(e -> jobBuilderService.entityFromDto(e))
                .collect(Collectors.toList());

        client.setJobs(jobs);

        return client;
    }
}
