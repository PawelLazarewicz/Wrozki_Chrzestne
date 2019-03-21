package pl.sda.wrozki_chrzestne_v_2.employee;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmployeeFacade {

    private EmployeeRepository employeeRepository;
    private EmployeeBuilderService employeeBuilderService;
}
