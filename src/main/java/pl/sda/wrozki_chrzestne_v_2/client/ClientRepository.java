package pl.sda.wrozki_chrzestne_v_2.client;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.wrozki_chrzestne_v_2.client.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
