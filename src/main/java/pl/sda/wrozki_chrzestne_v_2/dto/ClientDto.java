package pl.sda.wrozki_chrzestne_v_2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.wrozki_chrzestne_v_2.job.Job;

import java.util.ArrayList;
import java.util.List;

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

    private List<JobDto> jobs = new ArrayList<>();

    @Override
    public String toString() {
        return name + " " +
                lastName + "; " +
                city + "; " + address + "; " +
                postalCode + "; " +
                telephoneNumber + "; " +
                mail;
    }
}
