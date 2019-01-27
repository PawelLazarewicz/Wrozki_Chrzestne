package pl.sda.wrozki_chrzestne_v_2.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientBuilderService clientBuilderService;
    private Client editedClient;

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
        List<Client> clients = clientRepository.findAll();

        List<ClientDto> clientDtos = clients.stream()
                .map(e -> clientBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        model.addAttribute("clientsDtos", clientDtos);

        return "client/clientsHTML";
    }

    @RequestMapping("Client/{id}/show")
    public String getClient(@PathVariable Long id, Model model) {
        Client selectedClient = clientBuilderService.selectClient(id);

        ClientDto selectedClientDto = clientBuilderService.DtoFromEntity(selectedClient);

        model.addAttribute("client", selectedClientDto);

        return "client/clientHTML";
    }

    @RequestMapping("Client/{id}/delete")
    public String deleteClient(@PathVariable Long id, Model model){
        Client selectedClient = clientBuilderService.selectClient(id);

        ClientDto selectedClientDto = clientBuilderService.DtoFromEntity(selectedClient);

        clientRepository.delete(selectedClient);

        model.addAttribute("client", selectedClientDto);

        return "redirect:/Client/listClients";
    }

    @RequestMapping("Client/{id}/edit")
    public String editClient(@PathVariable Long id, Model model){
        editedClient = clientBuilderService.selectClient(id);
        ClientDto editedClientDto = clientBuilderService.DtoFromEntity(editedClient);

        model.addAttribute("editedClient", editedClientDto);

        return "client/updateClientHTML";
    }

    @RequestMapping(value = "/Client/updateClient", method = RequestMethod.POST)
    public String updateClient(@ModelAttribute ClientDto jobDto, Model model) {
        editedClient = clientBuilderService.updateEntityFromDto(jobDto, editedClient);
        clientRepository.save(editedClient);

        allClients(model);

        return "redirect:/Client/listClients";
    }
}
