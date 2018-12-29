package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientDto {

    private String name;
    private String lastName;
    private String city;

    private String address;
    private String postalCode;
    private String telephoneNumber;
    private String mail;
}
