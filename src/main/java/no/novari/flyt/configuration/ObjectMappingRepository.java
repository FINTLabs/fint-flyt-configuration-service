package no.novari.flyt.configuration;

import no.novari.flyt.configuration.model.configuration.entities.ObjectMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectMappingRepository extends JpaRepository<ObjectMapping, Long> {
}
