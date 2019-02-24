package pl.sda.wrozki_chrzestne_v_2.employee;

public enum EmployeeStatus {
    ACTIVE ("Active"),
    INACTIVE("Inactive");

    private final String name;

    EmployeeStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
