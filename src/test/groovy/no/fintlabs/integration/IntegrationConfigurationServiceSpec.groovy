package no.fintlabs.integration

import no.fintlabs.integration.model.configuration.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Specification

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=none")
@DirtiesContext
class IntegrationConfigurationServiceSpec extends Specification {

    private IntegrationConfigurationService integrationConfigurationService

    @Autowired
    private IntegrationConfigurationRepository integrationConfigurationRepository

    def setup() {
        integrationConfigurationService = new IntegrationConfigurationService(integrationConfigurationRepository)
    }

    private Configuration createConfiguration(String sourceApplicationIntegrationId) {
        Configuration integrationConfiguration = new Configuration()
        integrationConfiguration.setSourceApplicationIntegrationId(sourceApplicationIntegrationId)
        return integrationConfiguration
    }

    def "Creating a new IntegrationConfiguration should set id and set version to 1"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(
                createConfiguration("123")
        )

        when:
        def configurations = integrationConfigurationService.getIntegrationConfigurationById(configuration.getSourceApplicationIntegrationId())

        then:
        configurations.size() == 1
        configurations.get(0).getSourceApplicationIntegrationId() == "123"
        configurations.get(0).getVersion() == 1
    }

    def "When adding a new configuration version, the version should be incremented"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(
                createConfiguration("123")
        )

        when:
        integrationConfigurationService.addNewIntegrationConfigurationVersion(
                configuration.getSourceApplicationIntegrationId(),
                new Configuration(sourceApplicationIntegrationId: configuration.getSourceApplicationIntegrationId())
        )
        def versions = integrationConfigurationService.getIntegrationConfigurationById(
                configuration.getSourceApplicationIntegrationId()
        )

        then:
        versions.size() == 2
        versions.get(0).getVersion() == 2
        versions.get(1).getVersion() == 1
    }

    def "Deleting a configuration should remove all versions"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(
                createConfiguration("123")
        )

        when:
        integrationConfigurationService.addNewIntegrationConfigurationVersion(
                configuration.getSourceApplicationIntegrationId(),
                new Configuration(sourceApplicationIntegrationId: configuration.getSourceApplicationIntegrationId())
        )
        def versionsBeforeDelete = integrationConfigurationService.getIntegrationConfigurationById(
                configuration.getSourceApplicationIntegrationId()
        ).size()
        integrationConfigurationService.deleteIntegrationConfigurationById(configuration.getSourceApplicationIntegrationId())
        def versionsAfterDelete = integrationConfigurationService.getIntegrationConfigurationById(configuration.getSourceApplicationIntegrationId()).size()

        then:
        versionsBeforeDelete == 2
        versionsAfterDelete == 0
    }

    def "Get integration configuration by id should return all versions"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(
                createConfiguration("123")
        )
        when:
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration.getSourceApplicationIntegrationId(), new Configuration(sourceApplicationIntegrationId: configuration.getSourceApplicationIntegrationId()))
        def versions = integrationConfigurationService.getIntegrationConfigurationById(configuration.getSourceApplicationIntegrationId()).size()

        then:
        versions == 2
    }

    def "Get latest integration configurations should only return a list of latest configurations"() {
        given:
        def configuration1 = integrationConfigurationService.newIntegrationConfiguration(
                createConfiguration("1")
        )
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration1.getSourceApplicationIntegrationId(), new Configuration(sourceApplicationIntegrationId: configuration1.getSourceApplicationIntegrationId()))
        def configuration2 = integrationConfigurationService.newIntegrationConfiguration(
                createConfiguration("2")
        )
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration2.getSourceApplicationIntegrationId(), new Configuration(sourceApplicationIntegrationId: configuration2.getSourceApplicationIntegrationId()))

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
