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

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientBuilderService clientBuilderService;

    @Autowired
    private JobController jobController;

    private ClientDto selectedClientDto;
    private Map<Long, Long> activeJobsForClientMap = new HashMap<>();
    private Map<Long, Long> completedJobsForClientMap = new HashMap<>();

    @RequestMapping("/Client/addClient")
    public String addClientForm(Model model) {
        model.addAttribute("client", new ClientDto());
        return "client/addClientHTML";
    }

    @RequestMapping(value = "/Client/listClients", method = RequestMethod.POST)
    public String addClient(@ModelAttribute ClientDto clientDto, Model model) {
        Client newClient = clientBuilderService.entityFromDto(clientDto);
        clientRepository.save(newClient);

        allClients(model);

        return "redirect:/Client/listClients";
    }

    @RequestMapping("/Client/listClients")
    public String allClients(Model model) {

        List<ClientDto> allClientDtos = getAllClients();
        model.addAttribute("clientsDtos", allClientDtos);

        List<JobDto> uncompletedJobs = jobController.getUncompletedJobList();

        for (ClientDto clientDto : allClientDtos) {
            activeJobsForClientMap.put(clientDto.getId(), 0L);
        }

        for (ClientDto clientDto : allClientDtos) {
            Long activeJobsCounter = 0L;
            for (JobDto uncompletedJob : uncompletedJobs) {
                if (clientDto.getId().equals(uncompletedJob.getClient().getId())) {
                    for (Map.Entry entry : activeJobsForClientMap.entrySet()) {
                        if (entry.getKey().equals(clientDto.getId())) {
                            activeJobsCounter = activeJobsCounter + 1;
                            activeJobsForClientMap.replace(clientDto.getId(), activeJobsCounter);
                        }
                    }
                }
            }
        }

        model.addAttribute("activeJobsForClientMap", activeJobsForClientMap);

        List<JobDto> completeJobs = jobController.getCompletedJobList();

        for (ClientDto clientDto : allClientDtos) {
            completedJobsForClientMap.put(clientDto.getId(), 0L);
        }

        for (ClientDto clientDto : allClientDtos) {
            Long completedJobsCounter = 0L;
            for (JobDto completedJob : completeJobs) {
                if (clientDto.getId().equals(completedJob.getClient().getId())) {
                    for (Map.Entry entry : completedJobsForClientMap.entrySet()) {
                        if (entry.getKey().equals(clientDto.getId())) {
                            completedJobsCounter = completedJobsCounter + 1;
                            completedJobsForClientMap.replace(clientDto.getId(), completedJobsCounter);
                        }
                    }
                }
            }
        }

        model.addAttribute("completedJobsForClientMap", completedJobsForClientMap);

        return "client/clientsHTML";
    }

    @RequestMapping("Client/{id}/show")
    public String getClient(@PathVariable Long id, Model model) {
        Client selectedClient = clientBuilderService.selectClient(id);

        ClientDto selectedClientDto = clientBuilderService.dtoFromEntity(selectedClient);

        model.addAttribute("client", selectedClientDto);

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

        return "redirect:/Client/listClients";
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

        return "redirect:/Client/listClients";
    }

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(e -> clientBuilderService.dtoFromEntity(e))
                .collect(Collectors.toList());
    }
}
