package no.fintlabs.integration

import no.fintlabs.integration.model.IntegrationConfiguration
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification

@WebFluxTest(IntegrationConfigurationController.class)
@ContextConfiguration(classes = [IntegrationConfigurationService.class])
class IntegrationConfigurationControllerSpec extends Specification {

    IntegrationConfigurationService integrationConfigurationService

    @SpringBean
    IntegrationConfigurationRepository integrationConfigurationRepository = Mock()

    IntegrationConfigurationController controller

    WebTestClient client

    void setup() {
        integrationConfigurationService = new IntegrationConfigurationService(integrationConfigurationRepository)
        controller = new IntegrationConfigurationController(integrationConfigurationService)
        client = WebTestClient.bindToController(controller).build()
    }

    def "Get integration configurations should return a Page object with .content empty and .totalElements == 0"() {
        when:
        integrationConfigurationRepository.findAll() >> []

        then:
        client
                .get()
                .uri("/api/integration/configuration")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath('$.totalElements')
                .isEqualTo('0')
                .jsonPath('$.content.length()')
                .isEqualTo('0')
    }

    def "When creating a new integration configuration the response should be 201 with a location header to the created object"() {
        when:
        def response = client
                .post()
                .uri("/api/integration/configuration")
                .body(Mono.just(new IntegrationConfiguration()), IntegrationConfiguration.class)
                .exchange()


        then:
        integrationConfigurationRepository.save(_ as IntegrationConfiguration)
                >> new IntegrationConfiguration(id: "123")

        response.expectStatus()
                .isCreated()
                .expectHeader()
                .location("/api/integration/configuration/123")
    }

    def "Adding a new integration configuration to an existing config should return 200"() {
        when:
        def response = client
                .put()
                .uri("/api/integration/configuration/123")
                .body(Mono.just(new IntegrationConfiguration(id: "123")), IntegrationConfiguration.class)
                .exchange()

        then:
        integrationConfigurationRepository.save(_ as IntegrationConfiguration)
                >> new IntegrationConfiguration()
        integrationConfigurationRepository.getIntegrationConfigurationByIdOrderByVersionDesc(_ as String)
                >> [new IntegrationConfiguration()]

        response
                .expectStatus()
                .isOk()
    }

    def "Adding a new integration configuration to an non-existing config should return 404 "() {
        when:
        def response = client
                .put()
                .uri("/api/integration/configuration/321")
                .body(Mono.just(new IntegrationConfiguration(id: "123")), IntegrationConfiguration.class)
                .exchange()

        then:
        integrationConfigurationRepository.save(_ as IntegrationConfiguration)
                >> new IntegrationConfiguration()
        integrationConfigurationRepository.getIntegrationConfigurationByIdOrderByVersionDesc(_ as String)
                >> [new IntegrationConfiguration()]

        response
                .expectStatus()
                .isNotFound()
    }

    def "Deleting a integration configuration should return 200"() {
        expect:
        client
                .delete()
                .uri("/api/integration/configuration/321")
                .exchange()
                .expectStatus()
                .isOk()
    }

    def "Get integration configuration by id should return 200"() {
        when:
        def response = client
                .get()
                .uri("/api/integration/configuration/321")
                .exchange()

        then:
        integrationConfigurationRepository.getIntegrationConfigurationByIdOrderByVersionDesc(_ as String)
                >> [new IntegrationConfiguration()]

        response
                .expectStatus()
                .isOk()
    }

    def "Get integration configuration by non-existing id should return 404"() {
        when:
        def response = client
                .get()
                .uri("/api/integration/configuration/543")
                .exchange()

        then:
        integrationConfigurationRepository.getIntegrationConfigurationByIdOrderByVersionDesc(_ as String)
                >> []

        response
                .expectStatus()
                .isNotFound()
    }

    def "Get integration configuration by id and version should return 200"() {
        when:
        def response = client
                .get()
                .uri("/api/integration/configuration/321/2")
                .exchange()

        then:
        integrationConfigurationRepository.getIntegrationConfigurationByIdAndVersion(_ as String, _ as Integer)
                >> new IntegrationConfiguration()

        response
                .expectStatus()
                .isOk()
    }

    def "Get integration configuration by non-existing id or version should return 404"() {
        when:
        def response = client
                .get()
                .uri("/api/integration/configuration/999/99")
                .exchange()

        then:
        integrationConfigurationRepository.getIntegrationConfigurationByIdAndVersion(_ as String, _ as Integer)
                >> null

        response
                .expectStatus()
                .isNotFound()
    }

    def "Get latest integration configuration by id should return 200"() {
        when:
        def response = client
                .get()
                .uri("/api/integration/configuration/999/latest")
                .exchange()

        then:
        integrationConfigurationRepository.getIntegrationConfigurationByIdOrderByVersionDesc(_ as String)
                >> [new IntegrationConfiguration()]

        response
                .expectStatus()
                .isOk()
    }

    def "Get latest integration configuration by non-existing id should return 404"() {
        when:
        def response = client
                .get()
                .uri("/api/integration/configuration/888/latest")
                .exchange()

        then:
        integrationConfigurationRepository.getIntegrationConfigurationByIdOrderByVersionDesc(_ as String)
                >> []

        response
                .expectStatus()
                .isNotFound()
    }

}
