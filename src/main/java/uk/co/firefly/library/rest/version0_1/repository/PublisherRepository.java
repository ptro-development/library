package uk.co.firefly.library.rest.version0_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.firefly.library.rest.version0_1.model.Publisher;;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
