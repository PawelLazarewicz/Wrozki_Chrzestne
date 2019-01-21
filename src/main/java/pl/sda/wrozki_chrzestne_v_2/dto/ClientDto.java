package pl.sda.wrozki_chrzestne_v_2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientDto {

    private Long id;

    private String name;
    private String lastName;
    private String city;

    private String address;
    private String postalCode;
    private String telephoneNumber;
    private String mail;
}
