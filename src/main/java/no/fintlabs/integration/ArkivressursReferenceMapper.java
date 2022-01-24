package no.fintlabs.integration;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.felles.PersonResource;
import no.fintlabs.integration.model.ResourceReference;
import no.fintlabs.kafka.consumer.cache.FintCacheManager;
import no.fintlabs.kafka.consumer.cache.exceptions.NoSuchCacheEntryException;
import no.fintlabs.kafka.consumer.cache.exceptions.NoSuchCacheException;
import no.fintlabs.kafka.util.links.NoSuchLinkException;
import no.fintlabs.kafka.util.links.ResourceLinkUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
public class ArkivressursReferenceMapper {

    private final FintCacheManager fintCacheManager;

    public ArkivressursReferenceMapper(FintCacheManager fintCacheManager) {
        this.fintCacheManager = fintCacheManager;
    }

    public Optional<ResourceReference> map(ArkivressursResource arkivressursResource) {
        try {
            return Optional.of(new ResourceReference(
                    ResourceLinkUtil.getFirstSelfLink(arkivressursResource),
                    this.getPersonnavn(arkivressursResource)
            ));
        } catch (NoSuchLinkException | NoSuchCacheException | NoSuchCacheEntryException e) {
            return Optional.empty();
        }
    }

    private String getPersonnavn(ArkivressursResource arkivressursResource) {
        String personalressursResourceHref = this.getPersonalressursResourceHref(arkivressursResource);
        PersonalressursResource personalressursResource = this.getPersonalressursResource(personalressursResourceHref);

        String personResourceHref = this.getPersonResourceHref(personalressursResource);
        PersonResource personResource = this.getPersonResource(personResourceHref);

        Personnavn personnavn = personResource.getNavn();
        if (personnavn == null) {
            throw new IllegalStateException("Person resource contains no name");
        }
        return Stream.of(
                        personnavn.getFornavn(),
                        personnavn.getMellomnavn(),
                        personnavn.getEtternavn()
                ).filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

    private String getPersonalressursResourceHref(ArkivressursResource arkivressursResource) {
        return ResourceLinkUtil.getFirstLink(arkivressursResource::getPersonalressurs, arkivressursResource, "Personalressurs");
    }

    private String getPersonResourceHref(PersonalressursResource personalressursResource) {
        return ResourceLinkUtil.getFirstLink(personalressursResource::getPerson, personalressursResource, "Person");
    }

    private PersonalressursResource getPersonalressursResource(String personalressursResourceHref) {
        return this.fintCacheManager
                .getCache("administrasjon.personal.personalressurs", String.class, PersonalressursResource.class)
                .get(personalressursResourceHref);
    }

    private PersonResource getPersonResource(String personResourceHref) {
        return this.fintCacheManager
                .getCache("administrasjon.personal.person", String.class, PersonResource.class)
                .get(personResourceHref);
    }

}
