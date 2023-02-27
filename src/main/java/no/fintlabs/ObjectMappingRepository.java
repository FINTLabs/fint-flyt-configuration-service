package no.fintlabs;

import no.fintlabs.model.configuration.entities.object.ObjectMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectMappingRepository extends JpaRepository<ObjectMapping, Long> {
}
