package pl.sda.wrozki_chrzestne_v_2.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;
import pl.sda.wrozki_chrzestne_v_2.dto.JobDto;
import pl.sda.wrozki_chrzestne_v_2.job.JobController;

import java.util.ArrayList;
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

    private ClientDto selectedClient;
    private Map<Long, List<JobDto>> activeJobsForClientMap = new HashMap<>();
    private Map<Long, List<JobDto>> completedJobsForClientMap = new HashMap<>();

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
            activeJobsForClientMap.put(clientDto.getId(), new ArrayList<>());
        }

        for (ClientDto clientDto : allClientDtos) {
            for (JobDto uncompletedJob : uncompletedJobs) {
                if (clientDto.getId().equals(uncompletedJob.getClient().getId())) {
                    for (Map.Entry entry : activeJobsForClientMap.entrySet()) {
                        if (entry.getKey().equals(clientDto.getId())) {
                            activeJobsForClientMap.get(entry.getKey()).add(uncompletedJob);
                        }
                    }
                }
            }
        }

        model.addAttribute("activeJobsForClientMap", activeJobsForClientMap);

        List<JobDto> completeJobs = jobController.getCompletedJobList();

        for (ClientDto clientDto : allClientDtos) {
            completedJobsForClientMap.put(clientDto.getId(), new ArrayList<>());
        }

        for (ClientDto clientDto : allClientDtos) {
            for (JobDto completedJob : completeJobs) {
                if (clientDto.getId().equals(completedJob.getClient().getId())) {
                    for (Map.Entry entry : completedJobsForClientMap.entrySet()) {
                        if (entry.getKey().equals(clientDto.getId())) {
                            completedJobsForClientMap.get(entry.getKey()).add(completedJob);
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
        ClientDto selectedClientDto = clientBuilderService.dtoFromEntity(selectedClient);

        if (activeJobsForClientMap.get(selectedClient.getId()).isEmpty()) {
            clientRepository.delete(selectedClient);
        }


        model.addAttribute("client", selectedClientDto);

        return "redirect:/Client/listClients";
    }

    @RequestMapping("Client/{id}/edit")
    public String editClient(@PathVariable Long id, Model model) {
        Client clientToEdit = clientBuilderService.selectClient(id);
        selectedClient = clientBuilderService.dtoFromEntity(clientToEdit);

        model.addAttribute("selectedClient", selectedClient);

        return "client/updateClientHTML";
    }

    @RequestMapping(value = "/Client/updateClient", method = RequestMethod.POST)
    public String updateClient(@ModelAttribute ClientDto clientDto, Model model) {
        Client editedClientToSave = clientBuilderService.selectClientFromDto(selectedClient);
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
