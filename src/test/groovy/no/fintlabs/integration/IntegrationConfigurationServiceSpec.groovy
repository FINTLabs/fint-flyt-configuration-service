package no.fintlabs.integration

import no.fintlabs.integration.model.IntegrationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.util.StringUtils
import spock.lang.Specification

@DataJpaTest
@DirtiesContext
class IntegrationConfigurationServiceSpec extends Specification {

    private IntegrationConfigurationService integrationConfigurationService

    @Autowired
    private IntegrationConfigurationRepository integrationConfigurationRepository

    void setup() {
        integrationConfigurationService = new IntegrationConfigurationService(integrationConfigurationRepository)
    }

    void cleanup() {
        integrationConfigurationRepository.deleteAll()
    }

    def "Creating a new IntegrationConfiguration should set id and set version to 1"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(new IntegrationConfiguration())

        when:
        def configurations = integrationConfigurationService.getIntegrationConfigurationById(configuration.getIntegrationId())

        then:
        configurations.size() == 1
        StringUtils.hasText(configurations.get(0).getIntegrationId())
        configurations.get(0).getVersion() == 1
    }

    def "When adding a new configuration version version should be incremented"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(new IntegrationConfiguration())

        when:
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration.getIntegrationId(), new IntegrationConfiguration(integrationId: configuration.getIntegrationId()))
        def versions = integrationConfigurationService.getIntegrationConfigurationById(configuration.getIntegrationId())

        then:
        versions.size() == 2
        versions.get(0).getVersion() == 2
        versions.get(1).getVersion() == 1
    }

    def "Deleting a configuration should remove all versions"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(new IntegrationConfiguration())

        when:
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration.getIntegrationId(), new IntegrationConfiguration(integrationId: configuration.getIntegrationId()))
        def versionsBerforeDelete = integrationConfigurationService.getIntegrationConfigurationById(configuration.getIntegrationId()).size()
        integrationConfigurationService.deleteIntegrationConfigurationById(configuration.getIntegrationId())
        def versionsAfterDelete = integrationConfigurationService.getIntegrationConfigurationById(configuration.getIntegrationId()).size()

        then:
        versionsBerforeDelete == 2
        versionsAfterDelete == 0
    }

    def "Get integration configuration by id should return all versions"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(new IntegrationConfiguration())

        when:
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration.getIntegrationId(), new IntegrationConfiguration(integrationId: configuration.getIntegrationId()))
        def versions = integrationConfigurationService.getIntegrationConfigurationById(configuration.getIntegrationId()).size()

        then:
        versions == 2
    }

    def "Get latest integration configurations should only return a list of latest configurations"() {

        def configuration1 = integrationConfigurationService
                .newIntegrationConfiguration(new IntegrationConfiguration())
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration1.getIntegrationId(), new IntegrationConfiguration(integrationId: configuration1.getIntegrationId()))
        def configuration2 = integrationConfigurationService
                .newIntegrationConfiguration(new IntegrationConfiguration())
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration2.getIntegrationId(), new IntegrationConfiguration(integrationId: configuration2.getIntegrationId()))

        when:
        def allIntegrationConfigurations = integrationConfigurationService
                .getAllIntegrationConfiguration()
        def latestIntegrationConfigurations = integrationConfigurationService
                .getLatestIntegrationConfigurations()

        then:
        allIntegrationConfigurations.size() == 4
        latestIntegrationConfigurations.size() == 2
        latestIntegrationConfigurations.get(0).getVersion() == 2
        latestIntegrationConfigurations.get(1).getVersion() == 2
    }
}
