package pl.sda.wrozki_chrzestne_v_2;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmployeeController {

    private EmployeeRepository employeeRepository;
}
