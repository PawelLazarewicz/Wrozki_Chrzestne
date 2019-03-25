package pl.sda.wrozki_chrzestne_v_2.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;

import java.util.List;

@Controller
public class ClientController {

    private ClientFacade clientFacade;

    @Autowired
    public void setClientFacade(ClientFacade clientFacade) {
        this.clientFacade = clientFacade;
    }

    private static final String CLIENT_LIST = "redirect:/Client/listClients";
    private ClientDto selectedClientDto;

    @RequestMapping("/Client/addClient")
    public String addClientForm(Model model) {
        model.addAttribute("newClient", new ClientDto());

        return "client/addClientHTML";
    }

    @RequestMapping(value = "/Client/listClients", method = RequestMethod.POST)
    public String addClient(@ModelAttribute ClientDto clientDto, Model model) {
        clientFacade.addClient(clientDto);
        allClients(model);

        return CLIENT_LIST;
    }

    @RequestMapping("/Client/listClients")
    public String allClients(Model model) {
        model.addAttribute("clientsDtos", clientFacade.allClients());

        model.addAttribute("countOfActiveJobsForClient", clientFacade.countOfActiveJobsForClient());

        model.addAttribute("countOfCompletedJobsForClient", clientFacade.countOfCompletedJobsForClient());

        return "client/clientsHTML";
    }

    @RequestMapping("Client/{id}/show")
    public String getClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientFacade.getClient(id));

        return "client/clientHTML";
    }

    @RequestMapping("Client/{id}/delete")
    public String deleteClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientFacade.deleteClient(id));

        return CLIENT_LIST;
    }

    @RequestMapping("Client/{id}/edit")
    public String editClient(@PathVariable Long id, Model model) {
        selectedClientDto = clientFacade.editClient(id);
        model.addAttribute("selectedClient", clientFacade.editClient(id));

        return "client/updateClientHTML";
    }

    @RequestMapping(value = "/Client/updateClient", method = RequestMethod.POST)
    public String updateClient(@ModelAttribute ClientDto clientDto, Model model) {
        clientFacade.updateClient(clientDto, selectedClientDto);
        allClients(model);

        return CLIENT_LIST;
    }

    public List<ClientDto> getAllClients() {
        return clientFacade.allClients();
    }
}