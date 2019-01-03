package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
