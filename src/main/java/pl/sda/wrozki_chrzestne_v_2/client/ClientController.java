package pl.sda.wrozki_chrzestne_v_2.client;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sda.wrozki_chrzestne_v_2.dto.ClientDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ClientController {

    private ClientRepository clientRepository;

    private ClientBuilderService clientBuilderService;

    @PostMapping("addClient")
    public ResponseEntity addClient(@RequestBody ClientDto clientDto) {
        Client newClient = clientBuilderService.entityFromDto(clientDto);
        clientRepository.save(newClient);

        ClientDto newClientDto = clientBuilderService.DtoFromEntity(newClient);
        return new ResponseEntity(newClientDto, HttpStatus.OK);
    }

    @GetMapping("listClients")
    public ResponseEntity allClients() {
        List<Client> clientList = clientRepository.findAll();
        List<ClientDto> clientDtos = clientList.stream()
                .map(e -> clientBuilderService.DtoFromEntity(e))
                .collect(Collectors.toList());

        return new ResponseEntity(clientDtos, HttpStatus.OK);
    }

    @GetMapping("Client/{id}")
    public ResponseEntity getClient(@PathVariable Long id) {
        Client selectedClient = clientRepository.getOne(id);

        ClientDto selectedClientDto = clientBuilderService.DtoFromEntity(selectedClient);

        return new ResponseEntity(selectedClientDto, HttpStatus.OK);
    }

    @GetMapping("Client/{id}/delete")
    public ResponseEntity deleteClient(@PathVariable Long id){
        Client selectedClient = clientRepository.getOne(id);

        ClientDto selectedClientDto = clientBuilderService.DtoFromEntity(selectedClient);

        clientRepository.delete(selectedClient);

        return new ResponseEntity(selectedClientDto, HttpStatus.OK);
    }
}
