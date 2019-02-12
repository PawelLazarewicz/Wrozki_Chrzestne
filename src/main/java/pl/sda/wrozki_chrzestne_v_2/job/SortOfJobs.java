package pl.sda.wrozki_chrzestne_v_2.job;

public enum SortOfJobs {
    URODZINY ("Urodziny"),
    WESELE ("Wesele"),
    FESTYN ("Festyn");

    private final String name;

    SortOfJobs(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
