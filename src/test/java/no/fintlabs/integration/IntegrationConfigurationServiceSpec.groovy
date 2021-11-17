package no.fintlabs.integration

import no.fintlabs.integration.model.IntegrationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.domain.PageRequest
import org.springframework.util.StringUtils
import spock.lang.Specification

@DataMongoTest
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
        def configurations = integrationConfigurationService.getIntegrationConfigurationById(configuration.getId())

        then:
        configurations.size() == 1
        StringUtils.hasText(configurations.get(0).getId())
        configurations.get(0).getVersion() == 1
    }

    def "When adding a new configuration version version should be incremented"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(new IntegrationConfiguration())

        when:
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration.getId(), configuration)
        def versions = integrationConfigurationService.getIntegrationConfigurationById(configuration.getId())

        then:
        versions.size() == 2
        versions.get(0).getVersion() == 2
        versions.get(1).getVersion() == 1
    }

    def "Deleting a configuration should remove all versions"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(new IntegrationConfiguration())

        when:
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration.getId(), configuration)
        def versionsBerforeDelete = integrationConfigurationService.getIntegrationConfigurationById(configuration.getId()).size()
        integrationConfigurationService.deleteIntegrationConfigurationById(configuration.getId())
        def versionsAfterDelete = integrationConfigurationService.getIntegrationConfigurationById(configuration.getId()).size()

        then:
        versionsBerforeDelete == 2
        versionsAfterDelete == 0
    }

    def "Get integration configuration should return all versions"() {
        given:
        def configuration = integrationConfigurationService.newIntegrationConfiguration(new IntegrationConfiguration())

        when:
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration.getId(), configuration)
        def versions = integrationConfigurationService.getIntegrationConfigurationById(configuration.getId()).size()

        then:
        versions == 2
    }

    def "Get latest i ntegration configurations should only return a list of latest configurations"() {

        def configuration1 = integrationConfigurationService
                .newIntegrationConfiguration(new IntegrationConfiguration())
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration1.getId(), configuration1)
        def configuration2 = integrationConfigurationService
                .newIntegrationConfiguration(new IntegrationConfiguration())
        integrationConfigurationService.addNewIntegrationConfigurationVersion(configuration2.getId(), configuration2)

        when:
        def allIntegrationConfigurations = integrationConfigurationService
                .getAllIntegrationConfiguration(PageRequest.of(0, 10))
        def latestIntegrationConfigurations = integrationConfigurationService
                .getLatestIntegrationConfigurations(PageRequest.of(0, 10))

        then:
        allIntegrationConfigurations.size() == 4
        latestIntegrationConfigurations.size() == 2
        latestIntegrationConfigurations.getContent().get(0).getVersion() == 2
        latestIntegrationConfigurations.getContent().get(1).getVersion() == 2
    }
}
