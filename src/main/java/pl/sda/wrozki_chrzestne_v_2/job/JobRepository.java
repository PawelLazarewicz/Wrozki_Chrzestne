package pl.sda.wrozki_chrzestne_v_2.job;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.wrozki_chrzestne_v_2.job.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
