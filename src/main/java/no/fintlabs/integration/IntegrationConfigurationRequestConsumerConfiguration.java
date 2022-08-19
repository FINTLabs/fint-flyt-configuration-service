package no.fintlabs.integration;

import no.fintlabs.integration.model.IntegrationConfiguration;
import no.fintlabs.kafka.common.topic.TopicCleanupPolicyParameters;
import no.fintlabs.kafka.requestreply.ReplyProducerRecord;
import no.fintlabs.kafka.requestreply.RequestConsumerFactoryService;
import no.fintlabs.kafka.requestreply.topic.RequestTopicNameParameters;
import no.fintlabs.kafka.requestreply.topic.RequestTopicService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class IntegrationConfigurationRequestConsumerConfiguration {

    @Bean
    ConcurrentMessageListenerContainer<String, String> integrationConfigurationRequestListener(
            RequestConsumerFactoryService requestConsumerFactoryService,
            IntegrationConfigurationRepository integrationConfigurationRepository,
            RequestTopicService requestTopicService
    ) {
        RequestTopicNameParameters topicNameParameters = RequestTopicNameParameters.builder()
                .resource("integration.configuration")
                .parameterName("source-application-integration-id")
                .build();

        requestTopicService.ensureTopic(topicNameParameters, 0, TopicCleanupPolicyParameters.builder().build());

        return requestConsumerFactoryService.createFactory(
                String.class,
                IntegrationConfiguration.class,
                consumerRecord -> ReplyProducerRecord.<IntegrationConfiguration>builder()
                        .value(integrationConfigurationRepository
                                .findFirstBySourceApplicationIntegrationIdOrderByVersionDesc(consumerRecord.value())
                                .orElse(null))
                        .build(),
                new CommonLoggingErrorHandler()
        ).createContainer(topicNameParameters);
    }

}
