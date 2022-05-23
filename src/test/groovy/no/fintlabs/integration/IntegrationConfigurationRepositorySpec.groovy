package no.fintlabs.integration

import no.fintlabs.integration.model.IntegrationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDateTime

// TODO: Fix schema support
@DataJpaTest
@DirtiesContext()
@Ignore
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
        Optional<IntegrationConfiguration> result = integrationConfigurationRepository.findFirstByIntegrationIdOrderByVersionDesc("integrationId1")

        then:
        result.isPresent()
        result.get().getIntegrationId() == "integrationId1"
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
        Optional<IntegrationConfiguration> result = integrationConfigurationRepository.findFirstByIntegrationIdOrderByVersionDesc("integrationId4")

        then:
        result.isEmpty()
    }

    private IntegrationConfiguration createIntegrationConfiguration(Long id, String integrationId, int version) {
        return new IntegrationConfiguration(
                id,
                LocalDateTime.now(),
                integrationId,
                "name",
                "description",
                "sourceApplication",
                "sourceApplicationIntegrationId",
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
