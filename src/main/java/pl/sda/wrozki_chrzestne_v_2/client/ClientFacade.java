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

    public ClientDto getClient(Long id) {
        Client selectedClient = clientBuilderService.selectClient(id);
        ClientDto clientToSelectDto = clientBuilderService.dtoFromEntity(selectedClient);

        return clientToSelectDto;
    }

    public ClientDto deleteClient(Long id) {
        Client deletedClient = clientBuilderService.selectClient(id);
        ClientDto deletedClientDto = clientBuilderService.dtoFromEntity(deletedClient);

        if (deletedClient.getJobs()
                .stream()
                .noneMatch(job -> job.getJobStatus().equals(JobStatus.ACTIVE))) {
            clientRepository.delete(deletedClient);
        }

        return deletedClientDto;
    }

    public ClientDto editClient(Long id) {
        Client editedClient = clientBuilderService.selectClient(id);
        ClientDto editedClientDto = clientBuilderService.dtoFromEntity(editedClient);
        return editedClientDto;
    }

    public void updateClient(ClientDto updatingClientDto, ClientDto selectingClientDto) {
        Client editedClientToSave = clientBuilderService.selectClientFromDto(selectingClientDto);
        editedClientToSave = clientBuilderService.updateEntityFromDto(updatingClientDto, editedClientToSave);
        clientRepository.save(editedClientToSave);
    }
}
