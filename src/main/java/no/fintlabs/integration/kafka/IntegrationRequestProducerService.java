package no.fintlabs.integration.kafka;

import no.fintlabs.integration.model.integration.Integration;
import no.fintlabs.kafka.common.topic.TopicCleanupPolicyParameters;
import no.fintlabs.kafka.requestreply.RequestProducer;
import no.fintlabs.kafka.requestreply.RequestProducerFactory;
import no.fintlabs.kafka.requestreply.RequestProducerRecord;
import no.fintlabs.kafka.requestreply.topic.ReplyTopicNameParameters;
import no.fintlabs.kafka.requestreply.topic.ReplyTopicService;
import no.fintlabs.kafka.requestreply.topic.RequestTopicNameParameters;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IntegrationRequestProducerService {

    private final RequestTopicNameParameters requestTopicNameParameters;
    private final RequestProducer<Long, Integration> requestProducer;

    public IntegrationRequestProducerService(
            @Value("${fint.kafka.application-id}") String applicationId,
            RequestProducerFactory requestProducerFactory,
            ReplyTopicService replyTopicService
    ) {
        ReplyTopicNameParameters replyTopicNameParameters = ReplyTopicNameParameters.builder()
                .applicationId(applicationId)
                .resource("integration")
                .build();

        replyTopicService.ensureTopic(replyTopicNameParameters, 0, TopicCleanupPolicyParameters.builder().build());

        this.requestTopicNameParameters = RequestTopicNameParameters.builder()
                .resource("integration")
                .parameterName("integration-id")
                .build();

        this.requestProducer = requestProducerFactory.createProducer(
                replyTopicNameParameters,
                Long.class,
                Integration.class
        );
    }

    public Optional<Integration> get(Long integrationId) {
        return requestProducer.requestAndReceive(
                RequestProducerRecord.<Long>builder()
                        .topicNameParameters(requestTopicNameParameters)
                        .value(integrationId)
                        .build()
        ).map(ConsumerRecord::value);
    }

}
