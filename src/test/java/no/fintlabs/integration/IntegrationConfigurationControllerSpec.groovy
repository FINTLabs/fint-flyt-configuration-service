package no.fintlabs.integration

import no.fintlabs.integration.model.IntegrationConfiguration
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification

@WebFluxTest(IntegrationConfigurationController.class)
@ContextConfiguration(classes = [IntegrationConfigurationService.class, IntegrationConfigurationRepository.class])
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

    def "Get integration configurations should return a Page object with .content empty and totalElements == 0"() {
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

    def "Name"() {
        when:
        integrationConfigurationRepository.save(new IntegrationConfiguration())
                >> new IntegrationConfiguration(id: "123")


        then:
        //>> new IntegrationConfiguration()
        client
                .post()
                .uri("/api/integration/configuration")
                .body(Mono.just(new IntegrationConfiguration()), IntegrationConfiguration.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .value('Location', h -> h.endsWith("sdf"))
    }
}
