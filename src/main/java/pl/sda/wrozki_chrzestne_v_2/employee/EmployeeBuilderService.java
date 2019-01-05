package pl.sda.wrozki_chrzestne_v_2.employee;

import pl.sda.wrozki_chrzestne_v_2.dto.EmployeeDto;

public class EmployeeBuilderService {

    public Employee entityFromDto(EmployeeDto employeeDto) {
        Employee employee = new Employee();

        employee.setId(null);
        employee.setName(employeeDto.getName());
        employee.setLastName(employeeDto.getLastName());
        employee.setCity(employeeDto.getCity());
        employee.setAge(employeeDto.getAge());
        employee.setTelephoneNumber(employeeDto.getTelephoneNumber());
        employee.setMail(employeeDto.getMail());

        return employee;
    }

    public EmployeeDto DtoFromEntity(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setName(employee.getName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setCity(employee.getCity());
        employeeDto.setAge(employee.getAge());
        employeeDto.setTelephoneNumber(employee.getTelephoneNumber());
        employeeDto.setMail(employee.getMail());

        return employeeDto;
    }
}
