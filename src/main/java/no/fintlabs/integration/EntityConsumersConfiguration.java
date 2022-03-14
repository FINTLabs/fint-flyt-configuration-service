package no.fintlabs.integration;

import no.fint.model.resource.FintLinks;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fint.model.resource.felles.PersonResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.cache.FintCacheManager;
import no.fintlabs.kafka.entity.EntityTopicNameParameters;
import no.fintlabs.kafka.entity.FintKafkaEntityConsumerFactory;
import no.fintlabs.links.ResourceLinkUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class EntityConsumersConfiguration {

    @Value("${fint.org-id}")
    private String orgId;

    private final FintCacheManager fintCacheManager;
    private final FintKafkaEntityConsumerFactory entityConsumerFactory;


    public EntityConsumersConfiguration(FintCacheManager fintCacheManager, FintKafkaEntityConsumerFactory entityConsumerFactory) {
        this.fintCacheManager = fintCacheManager;
        this.entityConsumerFactory = entityConsumerFactory;
    }

    private <T extends FintLinks> ConcurrentMessageListenerContainer<String, T> createCacheConsumer(
            String resourceReference,
            Class<T> resourceClass
    ) {
        FintCache<String, T> cache = fintCacheManager.createCache(
                resourceReference,
                String.class,
                resourceClass
        );
        return entityConsumerFactory.createConsumer(
                EntityTopicNameParameters.builder()
                        .orgId(orgId)
                        .domainContext("skjema")
                        .resource(resourceReference)
                        .build(),
                resourceClass,
                consumerRecord -> cache.put(
                        ResourceLinkUtil.getSelfLinks(consumerRecord.value()),
                        consumerRecord.value()
                ),
                new CommonLoggingErrorHandler()
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, AdministrativEnhetResource> administrativEnhetResourceEntityConsumer() {
        return createCacheConsumer("arkiv.noark.administrativenhet", AdministrativEnhetResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, ArkivdelResource> arkivdelResourceEntityConsumer() {
        return createCacheConsumer("arkiv.noark.arkivdel", ArkivdelResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, ArkivressursResource> arkivressursResourceEntityConsumer() {
        return createCacheConsumer("arkiv.noark.arkivressurs", ArkivressursResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, DokumentStatusResource> dokumentStatusResourceEntityConsumer() {
        return createCacheConsumer("arkiv.kodeverk.dokumentstatus", DokumentStatusResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, DokumentTypeResource> dokumentTypeResourceEntityConsumer() {
        return createCacheConsumer("arkiv.kodeverk.dokumenttype", DokumentTypeResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, KlassifikasjonssystemResource> klassifikasjonssystemResourceEntityConsumer() {
        return createCacheConsumer("arkiv.noark.klassifikasjonssystem", KlassifikasjonssystemResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, RolleResource> rolleResourceEntityConsumer() {
        return createCacheConsumer("arkiv.kodeverk.rolle", RolleResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, SaksstatusResource> saksstatusResourceEntityConsumer() {
        return createCacheConsumer("arkiv.kodeverk.saksstatus", SaksstatusResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, SkjermingshjemmelResource> skjermingshjemmelResourceEntityConsumer() {
        return createCacheConsumer("arkiv.kodeverk.skjermingshjemmel", SkjermingshjemmelResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, TilgangsrestriksjonResource> tilgangsrestriksjonResourceEntityConsumer() {
        return createCacheConsumer("arkiv.kodeverk.tilgangsrestriksjon", TilgangsrestriksjonResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, PersonalressursResource> personalressursResourceEntityConsumer() {
        return createCacheConsumer("administrasjon.personal.personalressurs", PersonalressursResource.class);
    }

    @Bean
    ConcurrentMessageListenerContainer<String, PersonResource> personResourceEntityConsumer() {
        return createCacheConsumer("administrasjon.personal.person", PersonResource.class);
    }

}
