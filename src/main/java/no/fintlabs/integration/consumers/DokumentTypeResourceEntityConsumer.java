package no.fintlabs.integration.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.Link;
import no.fint.model.resource.arkiv.kodeverk.DokumentTypeResource;
import no.fintlabs.kafka.consumer.EntityConsumer;
import no.fintlabs.kafka.consumer.cache.FintCacheManager;
import no.fintlabs.kafka.topic.DomainContext;
import no.fintlabs.kafka.topic.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DokumentTypeResourceEntityConsumer extends EntityConsumer<DokumentTypeResource> {

    protected DokumentTypeResourceEntityConsumer(ObjectMapper objectMapper, FintCacheManager fintCacheManager) {
        super(objectMapper, fintCacheManager);
    }

    @Override
    protected String getResourceReference() {
        return "arkiv.kodeverk.dokumenttype";
    }

    @Override
    protected Class<DokumentTypeResource> getResourceClass() {
        return DokumentTypeResource.class;
    }

    @Override
    protected List<String> getKeys(DokumentTypeResource resource) {
        return Stream.concat(
                Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                resource.getSelfLinks().stream().map(Link::getHref)
        ).collect(Collectors.toList());
    }

    @Bean
    String dokumentTypeResourceEntityTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName(DomainContext.SKJEMA, this.getResourceReference());
    }

    @Override
    @KafkaListener(topics = "#{dokumentTypeResourceEntityTopicName}")
    protected void consume(ConsumerRecord<String, String> consumerRecord) {
        this.processMessage(consumerRecord);
    }

}
