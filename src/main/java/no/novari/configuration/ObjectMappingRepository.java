package no.novari.configuration;

import no.novari.configuration.model.configuration.entities.ObjectMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectMappingRepository extends JpaRepository<ObjectMapping, Long> {
}
