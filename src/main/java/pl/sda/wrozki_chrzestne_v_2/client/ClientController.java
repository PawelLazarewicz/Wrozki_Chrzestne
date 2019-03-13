package pl.sda.wrozki_chrzestne_v_2.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;
import pl.sda.wrozki_chrzestne_v_2.job.JobController;
import pl.sda.wrozki_chrzestne_v_2.job.JobStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ClientController {

    private static final String CLIENT_LIST = "redirect:/Client/listClients";
    private ClientRepository clientRepository;

    private ClientBuilderService clientBuilderService;

    private JobController jobController;

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setClientBuilderService(ClientBuilderService clientBuilderService) {
        this.clientBuilderService = clientBuilderService;
    }

    @Autowired
    public void setJobController(JobController jobController) {
        this.jobController = jobController;
    }

    private ClientDto selectedClientDto;
//    private Map<Long, Long> activeJobsForClientMap = new HashMap<>();
//    private Map<Long, Long> completedJobsForClientMap = new HashMap<>();

    @RequestMapping("/Client/addClient")
    public String addClientForm(Model model) {
        model.addAttribute("newClient", new ClientDto());
        return "client/addClientHTML";
    }

    @RequestMapping(value = "/Client/listClients", method = RequestMethod.POST)
    public String addClient(@ModelAttribute ClientDto clientDto, Model model) {
        Client newClient = clientBuilderService.entityFromDto(clientDto);
        clientRepository.save(newClient);

        allClients(model);

        return CLIENT_LIST;
    }

    @RequestMapping("/Client/listClients")
    public String allClients(Model model) {

        List<ClientDto> allClientDtos = getAllClients();
        model.addAttribute("clientsDtos", allClientDtos);

        //LAMBDA for displaying counter for client active jobs
        Map<Long, Long> countOfActiveJobsForClient = new HashMap<>();
        getAllClients()
                .forEach(clientDto -> countOfActiveJobsForClient.put(clientDto.getId(), clientDto.getJobs()
                        .stream()
                        .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.ACTIVE))
                        .count()));
        model.addAttribute("countOfActiveJobsForClient", countOfActiveJobsForClient);

        //LAMBDA for displaying counter for client completed jobs
        Map<Long, Long> countOfCompletedJobsForClient = new HashMap<>();
        getAllClients()
                .forEach(clientDto -> countOfCompletedJobsForClient.put(clientDto.getId(), clientDto.getJobs()
                        .stream()
                        .filter(jobDto -> jobDto.getJobStatus().equals(JobStatus.COMPLETED))
                        .count()));
        model.addAttribute("countOfCompletedJobsForClient", countOfCompletedJobsForClient);

//        List<JobDto> uncompletedJobs = jobController.getUncompletedJobList();
//
//        for (ClientDto clientDto : allClientDtos) {
//            activeJobsForClientMap.put(clientDto.getId(), 0L);
//        }
//
//        for (ClientDto clientDto : allClientDtos) {
//            Long activeJobsCounter = 0L;
//            for (JobDto uncompletedJob : uncompletedJobs) {
//                if (clientDto.getId().equals(uncompletedJob.getClient().getId())) {
//                    for (Map.Entry entry : activeJobsForClientMap.entrySet()) {
//                        if (entry.getKey().equals(clientDto.getId())) {
//                            activeJobsCounter = activeJobsCounter + 1;
//                            activeJobsForClientMap.replace(clientDto.getId(), activeJobsCounter);
//                        }
//                    }
//                }
//            }
//        }
//
//        model.addAttribute("activeJobsForClientMap", activeJobsForClientMap);
//
//        List<JobDto> completeJobs = jobController.getCompletedJobList();
//
//        for (ClientDto clientDto : allClientDtos) {
//            completedJobsForClientMap.put(clientDto.getId(), 0L);
//        }
//
//        for (ClientDto clientDto : allClientDtos) {
//            Long completedJobsCounter = 0L;
//            for (JobDto completedJob : completeJobs) {
//                if (clientDto.getId().equals(completedJob.getClient().getId())) {
//                    for (Map.Entry entry : completedJobsForClientMap.entrySet()) {
//                        if (entry.getKey().equals(clientDto.getId())) {
//                            completedJobsCounter = completedJobsCounter + 1;
//                            completedJobsForClientMap.replace(clientDto.getId(), completedJobsCounter);
//                        }
//                    }
//                }
//            }
//        }
//
//        model.addAttribute("completedJobsForClientMap", completedJobsForClientMap);

        return "client/clientsHTML";
    }

    @RequestMapping("Client/{id}/show")
    public String getClient(@PathVariable Long id, Model model) {
        Client selectedClient = clientBuilderService.selectClient(id);
        ClientDto clientToselectDto = clientBuilderService.dtoFromEntity(selectedClient);

        model.addAttribute("client", clientToselectDto);

        return "client/clientHTML";
    }

    @RequestMapping("Client/{id}/delete")
    public String deleteClient(@PathVariable Long id, Model model) {
        Client selectedClient = clientBuilderService.selectClient(id);
        selectedClientDto = clientBuilderService.dtoFromEntity(selectedClient);

        if (selectedClient.getJobs()
                .stream()
                .noneMatch(job -> job.getJobStatus().equals(JobStatus.ACTIVE))) {
            clientRepository.delete(selectedClient);
        }

        model.addAttribute("client", selectedClientDto);

        return CLIENT_LIST;
    }

    @RequestMapping("Client/{id}/edit")
    public String editClient(@PathVariable Long id, Model model) {
        Client clientToEdit = clientBuilderService.selectClient(id);
        selectedClientDto = clientBuilderService.dtoFromEntity(clientToEdit);

        model.addAttribute("selectedClient", selectedClientDto);

        return "client/updateClientHTML";
    }

    @RequestMapping(value = "/Client/updateClient", method = RequestMethod.POST)
    public String updateClient(@ModelAttribute ClientDto clientDto, Model model) {
        Client editedClientToSave = clientBuilderService.selectClientFromDto(selectedClientDto);
        editedClientToSave = clientBuilderService.updateEntityFromDto(clientDto, editedClientToSave);
        clientRepository.save(editedClientToSave);

        allClients(model);

        return CLIENT_LIST;
    }

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(e -> clientBuilderService.dtoFromEntityWithJobs(e))
                .collect(Collectors.toList());
    }
}
