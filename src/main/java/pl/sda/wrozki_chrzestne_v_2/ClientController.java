package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
