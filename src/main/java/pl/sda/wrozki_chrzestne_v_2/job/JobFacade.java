package pl.sda.wrozki_chrzestne_v_2.job;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JobFacade {

    private JobRepository jobRepository;
    private JobBuilderService jobBuilderService;
}
