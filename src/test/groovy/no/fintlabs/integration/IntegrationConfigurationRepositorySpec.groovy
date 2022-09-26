package no.fintlabs.integration

import no.fintlabs.integration.model.configuration.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=none")
@DirtiesContext()
class IntegrationConfigurationRepositorySpec extends Specification {

    @Autowired
    IntegrationConfigurationRepository integrationConfigurationRepository

    def 'should return the configuration with the highest version number for the integration'() {
        given:
        integrationConfigurationRepository.saveAll(List.of(
                createIntegrationConfiguration(5L, "integrationId2", 1),
                createIntegrationConfiguration(1L, "integrationId1", 1),
                createIntegrationConfiguration(2L, "integrationId1", 3),
                createIntegrationConfiguration(3L, "integrationId1", 2),
                createIntegrationConfiguration(6L, "integrationId3", 1)
        ))

        when:
        Optional<Configuration> result = integrationConfigurationRepository.findFirstBySourceApplicationIntegrationIdOrderByVersionDesc("integrationId1")

        then:
        result.isPresent()
        result.get().getSourceApplicationIntegrationId() == "integrationId1"
        result.get().getId() == 2L
    }

    def 'should return empty optional if no configuration for the integration exists'() {
        given:
        integrationConfigurationRepository.saveAll(List.of(
                createIntegrationConfiguration(5L, "integrationId2", 1),
                createIntegrationConfiguration(1L, "integrationId1", 1),
                createIntegrationConfiguration(2L, "integrationId1", 3),
                createIntegrationConfiguration(3L, "integrationId1", 2),
                createIntegrationConfiguration(6L, "integrationId3", 1)
        ))

        when:
        Optional<Configuration> result = integrationConfigurationRepository.findFirstBySourceApplicationIntegrationIdOrderByVersionDesc("integrationId4")

        then:
        result.isEmpty()
    }

    private Configuration createIntegrationConfiguration(Long id, String sourceApplicationIntegrationId, int version) {
        return new Configuration(
                id,
                LocalDateTime.now(),
                "name",
                "description",
                "sourceApplicationId",
                sourceApplicationIntegrationId,
                "orgId",
                "destination",
                version,
                true,
                null,
                null,
                null,
                null
        )
    }
}
