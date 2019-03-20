package pl.sda.wrozki_chrzestne_v_2.client;

import lombok.AllArgsConstructor;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;
import pl.sda.wrozki_chrzestne_v_2.job.JobStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ClientFacade {

    private ClientBuilderService clientBuilderService;
    private ClientRepository clientRepository;

    public void addClient(ClientDto clientDto) {

        Client newClient = clientBuilderService.entityFromDto(clientDto);
        clientRepository.save(newClient);

        //return clientBuilderService.dtoFromEntityWithJobs(savedClient);
    }

    public List<ClientDto> allClients() {
        return getAllClients();
    }

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(e -> clientBuilderService.dtoFromEntityWithJobs(e))
                .collect(Collectors.toList());
    }

    public Map<Long, Long> countOfActiveJobsForClient() {

        //LAMBDA for displaying counter for client active jobs
        Map<Long, Long> countOfActiveJobsForClient = new HashMap<>();
        allClients()
                .forEach(clientDto -> countOfActiveJobsForClient.put(clientDto.getId(), clientDto.getJobs()
                        .stream()
                        .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.ACTIVE))
                        .count()));

        return countOfActiveJobsForClient;
    }

    public Map<Long, Long> countOfCompletedJobsForClient() {

        //LAMBDA for displaying counter for client completed jobs
        Map<Long, Long> countOfCompletedJobsForClient = new HashMap<>();
        allClients()
                .forEach(clientDto -> countOfCompletedJobsForClient.put(clientDto.getId(), clientDto.getJobs()
                        .stream()
                        .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.COMPLETED))
                        .count()));

        return countOfCompletedJobsForClient;
    }
}
