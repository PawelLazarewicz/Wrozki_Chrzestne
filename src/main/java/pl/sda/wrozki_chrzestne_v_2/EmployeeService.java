package pl.sda.wrozki_chrzestne_v_2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeService extends JpaRepository<Employee, Long> {
}
