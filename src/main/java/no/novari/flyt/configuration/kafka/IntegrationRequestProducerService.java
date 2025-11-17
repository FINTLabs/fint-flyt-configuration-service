package no.novari.flyt.configuration.kafka;

import no.novari.flyt.configuration.model.integration.Integration;
import no.novari.kafka.consuming.ListenerConfiguration;
import no.novari.kafka.requestreply.RequestProducerRecord;
import no.novari.kafka.requestreply.RequestTemplate;
import no.novari.kafka.requestreply.RequestTemplateFactory;
import no.novari.kafka.requestreply.topic.ReplyTopicService;
import no.novari.kafka.requestreply.topic.configuration.ReplyTopicConfiguration;
import no.novari.kafka.requestreply.topic.name.ReplyTopicNameParameters;
import no.novari.kafka.requestreply.topic.name.RequestTopicNameParameters;
import no.novari.kafka.topic.name.TopicNamePrefixParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class IntegrationRequestProducerService {

    private final RequestTopicNameParameters requestTopicNameParameters;
    private final RequestTemplate<Long, Integration> requestTemplate;

    private static final Duration RETENTION_TIME = Duration.ofMinutes(5);
    private static final Duration REPLY_TIMEOUT = Duration.ofSeconds(5);

    public IntegrationRequestProducerService(
            @Value("${novari.kafka.application-id}") String applicationId,
            RequestTemplateFactory requestTemplateFactory,
            ReplyTopicService replyTopicService
    ) {
        ReplyTopicNameParameters replyTopicNameParameters = ReplyTopicNameParameters
                .builder()
                .topicNamePrefixParameters(TopicNamePrefixParameters
                        .stepBuilder()
                        .orgIdApplicationDefault()
                        .domainContextApplicationDefault()
                        .build()
                )
                .applicationId(applicationId)
                .resourceName("integration")
                .build();

        replyTopicService.createOrModifyTopic(replyTopicNameParameters, ReplyTopicConfiguration
                .builder()
                .retentionTime(RETENTION_TIME)
                .build());

        this.requestTopicNameParameters = RequestTopicNameParameters
                .builder()
                .topicNamePrefixParameters(TopicNamePrefixParameters
                        .stepBuilder()
                        .orgIdApplicationDefault()
                        .domainContextApplicationDefault()
                        .build()
                )
                .resourceName("integration")
                .parameterName("integration-id")
                .build();

        this.requestTemplate = requestTemplateFactory.createTemplate(
                replyTopicNameParameters,
                Long.class,
                Integration.class,
                REPLY_TIMEOUT,
                ListenerConfiguration
                        .stepBuilder()
                        .groupIdApplicationDefault()
                        .maxPollRecordsKafkaDefault()
                        .maxPollIntervalKafkaDefault()
                        .continueFromPreviousOffsetOnAssignment()
                        .build()
        );
    }

    public Optional<Integration> get(Long integrationId) {
        return Optional.ofNullable(
                requestTemplate.requestAndReceive(
                        RequestProducerRecord.<Long>builder()
                                .topicNameParameters(requestTopicNameParameters)
                                .value(integrationId)
                                .build()
                ).value()
        );
    }

}
