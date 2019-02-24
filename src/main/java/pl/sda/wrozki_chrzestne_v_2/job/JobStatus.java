package pl.sda.wrozki_chrzestne_v_2.job;

public enum JobStatus {

    ACTIVE ("Active"),
    COMPLETED("Completed");

    private final String name;

    JobStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
