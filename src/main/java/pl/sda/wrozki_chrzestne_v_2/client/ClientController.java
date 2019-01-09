package pl.sda.wrozki_chrzestne_v_2.client;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
