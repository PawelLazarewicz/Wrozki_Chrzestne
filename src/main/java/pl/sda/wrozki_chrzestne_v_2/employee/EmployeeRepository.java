package pl.sda.wrozki_chrzestne_v_2.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.wrozki_chrzestne_v_2.employee.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
