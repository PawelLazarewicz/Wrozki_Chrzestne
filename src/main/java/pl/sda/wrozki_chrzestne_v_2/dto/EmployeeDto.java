package pl.sda.wrozki_chrzestne_v_2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.wrozki_chrzestne_v_2.employee.EmployeeStatus;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDto {

    private Long id;

    private String name;
    private String lastName;
    private String city;

    private int age;
    private int telephoneNumber;
    private String mail;

    private EmployeeStatus employeeStatus = EmployeeStatus.ACTIVE;

    private List<JobDto> workedJobs = new ArrayList<>();
}
