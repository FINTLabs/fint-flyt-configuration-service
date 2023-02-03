package no.fintlabs;

import no.fintlabs.model.configuration.entities.ElementMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementMappingRepository extends JpaRepository<ElementMapping, Long> {
}
