package pl.sda.wrozki_chrzestne_v_2.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.wrozki_chrzestne_v_2.job.Job;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CLIENTS")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private String city;

    private String address;
    private String postalCode;
    private String telephoneNumber;
    private String mail;

    @OneToMany
    @JoinColumn(name = "CLIENT_ID")
    private List<Job> jobs = new ArrayList<>();
}
